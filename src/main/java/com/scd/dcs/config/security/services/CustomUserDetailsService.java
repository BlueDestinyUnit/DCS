package com.scd.dcs.config.security.services;


import com.scd.dcs.config.security.domains.SecurityUser;
import com.scd.dcs.domains.entities.UserEntity;
import com.scd.dcs.mappers.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // JPA 사용, Mybatis 사용시 mapper를 등록하셔서 user 정보를 받아오시면 됩니다.



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(" ======= loadUserByUsername: ["+ username + "]");

        UserEntity user = userMapper.selectUserByEmail(username);
        if(user== null){
            throw new UsernameNotFoundException("ERROR: USER NOT FOUND!");
        }


        GrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        return new SecurityUser(user, List.of(authorities));
    }
}