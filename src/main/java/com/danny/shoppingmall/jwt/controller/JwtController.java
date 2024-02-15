package com.danny.shoppingmall.jwt.controller;


import com.danny.shoppingmall.jwt.dto.JwtProperties;
import com.danny.shoppingmall.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/renew-access")
    public ResponseEntity<?> renewAccessToken(HttpServletRequest request) {

        return jwtService.renewAccessToken(request);

    }
}
