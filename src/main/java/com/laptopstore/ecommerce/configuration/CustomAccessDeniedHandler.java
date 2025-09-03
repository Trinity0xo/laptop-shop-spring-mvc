package com.laptopstore.ecommerce.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        request.setAttribute("code", HttpStatus.FORBIDDEN.value());
        request.setAttribute("message", "Bạn không có quyền truy cập vào tài nguyên này");
        request.setAttribute("error", "Forbidden");
        request.setAttribute("redirectUrl", "/");

        request.getRequestDispatcher("/forbidden").forward(request, response);
    }
}
