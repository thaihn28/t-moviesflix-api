package com.example.tmovierestapi.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

//    Now we create AuthEntryPointJwt class that implements AuthenticationEntryPoint interface.
//    Then we override the commence() method.
//    This method will be triggerd anytime unauthenticated User requests a secured HTTP resource and an AuthenticationException is thrown.

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "Bad credentials");
        response.getOutputStream().println("{ \"Unauthorized error\": "  +  authException.getMessage() + " \n }");
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//                authException.getLocalizedMessage());
    }
}
