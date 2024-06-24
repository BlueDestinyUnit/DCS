package com.scd.dcs.config.security;


import com.scd.dcs.config.security.authentications.CustomAuthenticationFilter;
import com.scd.dcs.config.security.authentications.CustomLoginAuthenticationEntryPoint;
import com.scd.dcs.config.security.handlers.*;
import com.scd.dcs.config.security.services.CustomUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity

public class CustomSecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomLoginAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final DataSource dataSource;


    @Autowired
    public CustomSecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                                CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                                CustomLoginAuthenticationEntryPoint authenticationEntryPoint,
                                AuthenticationConfiguration authenticationConfiguration,
                                CustomAccessDeniedHandler accessDeniedHandler,
                                DataSource dataSource) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationConfiguration = authenticationConfiguration;
        this.accessDeniedHandler = accessDeniedHandler;
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/main").authenticated()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").authenticated()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/board/*").authenticated()
                        .requestMatchers("/article/**").authenticated()
                        .requestMatchers("/article/image").permitAll()
                        .requestMatchers("/user/attendance").authenticated()
                        .requestMatchers("/user/*").permitAll()
                        .requestMatchers("/addWork").authenticated()
                        .requestMatchers("/work").authenticated()
                        .requestMatchers("/work/").authenticated()
                        .requestMatchers("/subImage").permitAll()
                        .requestMatchers("/updateImage").permitAll()
                        .requestMatchers("/admin").authenticated()
                        .requestMatchers("/admin/*").authenticated()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/libraries/**").permitAll()
                        .requestMatchers("/delete").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/logout/")
                        .addLogoutHandler(logoutHandler())
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .userDetailsService(customUserDetailsService)
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(60 * 60));
        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }



    @Bean
    public LogoutHandler logoutHandler(){
        return new CustomLogoutHandler();
    }


    @Bean
    public CustomAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        // **
        customAuthenticationFilter.setSecurityContextRepository(
                new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                ));

        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }

    //정적 자원들을 스프링 시큐리티 적용에서 제외시키겠다
    //예) /css/style.css 호출시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (webSecurity) -> webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    // https://curiousjinan.tistory.com/entry/spring-boot-3-1-security-6-security-config-class-detail-2 참조
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token", "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
