package com.danny.shoppingmall.jwt.filter;

import com.danny.shoppingmall.jwt.dto.JwtErrors;
import com.danny.shoppingmall.jwt.dto.JwtProperties;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = (String) request.getAttribute(JwtProperties.JWT_EXCEPTION);
        if(JwtErrors.EXPIRED_TOKEN.equals(message)) {
            setResponse(response, message);
        } else if (JwtErrors.WRONG_TOKEN.equals(message)) {
            setResponse(response, message);
        }
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        jsonObject.addProperty("code", 1);
        jsonObject.addProperty("message", message);
        response.getWriter().print(jsonObject);
    }
}
