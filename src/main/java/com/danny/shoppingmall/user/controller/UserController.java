package com.danny.shoppingmall.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.danny.shoppingmall.jwt.dto.JwtProperties;
import com.danny.shoppingmall.jwt.repository.RefreshTokenRepository;
import com.danny.shoppingmall.security.PrincipalDetails;
import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.User;
import com.danny.shoppingmall.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        return userService.signUp(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();
        System.out.println("로그아웃 실행");
        System.out.println("로그아웃에서 받은 엑세스 헤더" + request.getHeader(JwtProperties.ACCESS_HEADER_STRING));
        System.out.println("로그아웃에서 받은 프린시펄" + principalDetails.getUserDTO());
        try {
            String userId = principalDetails.getUsername();
            refreshTokenRepository.deleteById(userId);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
//            authentication.setAuthenticated(false);
//            System.out.println(authentication);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            returnMap.put("logout", "ok");
            System.out.println("로그아웃의 리턴맵" + returnMap);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok().body(responseDTO);
        }
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<?> checkId(@RequestParam String userEmail) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();
        System.out.println("이건 받아온 유저아이디" + userEmail);
        try {
            boolean checkUser = userService.checkUser(userEmail);
            System.out.println("이건 db조회로 찾은 유저가 있는지?" + checkUser);
            String status = checkUser ? "no" : "ok";
            returnMap.put("id", status);
            System.out.println("이건 리턴맵" + returnMap);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.ok().body(responseDTO);
        }
    }

    @PostMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("넌 왜 받아지는거임?");
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        Map<String, Object> returnMap = new HashMap<>();

        try {
            UserDTO userDTO = principalDetails.getUserDTO();
            System.out.println("받은 유저객체"+ userDTO);
            UserDTO userInfo = UserDTO.builder()
                    .userEmail(userDTO.getUserEmail())
                    .userName(userDTO.getUserName())
                    .userPhoneNum(userDTO.getUserPhoneNum())
                    .userRoles(userDTO.getUserRoles())
                    .build();
            returnMap.put("userInfo", userInfo);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
}
