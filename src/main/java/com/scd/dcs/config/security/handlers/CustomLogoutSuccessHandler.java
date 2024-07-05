package com.scd.dcs.config.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scd.dcs.results.CommonResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        System.out.println("로그아웃 석세스");
        // 로그아웃 성공 후의 추가 작업을 수행합니다.
        // 예를 들어, 로그아웃한 사용자의 세션을 종료하거나 리다이렉트를 수행할 수 있습니다.

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("result", CommonResult.SUCCESS.name().toLowerCase());

        // JSON 형식으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        // JSON 응답 보내기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

    }
}