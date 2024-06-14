package com.scd.dcs.config.security.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutHandler extends  SecurityContextLogoutHandler{

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("로그아웃 동작확인");
        System.out.println("requset:" + request.getRequestURI());
        System.out.println("response:" + response.toString());
        System.out.println();
        if (authentication != null) {
            System.out.println(authentication.isAuthenticated());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth :" + auth.toString());
        super.logout(request, response, auth);
    }
}