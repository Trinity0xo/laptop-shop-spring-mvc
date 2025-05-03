package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if(exception instanceof BadCredentialsException){
            errorMessage = "Invalid email or password";
        } else{
            errorMessage = "Something went wrong";
        }

        request.getSession().setAttribute("loginErrorMessage", errorMessage);

        response.sendRedirect("/login?error");
    }
}
