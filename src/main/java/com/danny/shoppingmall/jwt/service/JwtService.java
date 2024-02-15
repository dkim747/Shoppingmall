package com.danny.shoppingmall.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.danny.shoppingmall.jwt.dto.JwtErrors;
import com.danny.shoppingmall.jwt.dto.JwtProperties;
import com.danny.shoppingmall.jwt.entity.RefreshToken;
import com.danny.shoppingmall.jwt.repository.RefreshTokenRepository;
import com.danny.shoppingmall.redis.service.RedisService;
import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.User;
import com.danny.shoppingmall.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;

    public String createAccessToken(String userName, String userEmail) {

        String accessToken = JWT.create()
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("userEmail", userEmail)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return accessToken;

    }

    public String createRefreshToken(String userEmail) {

        String refreshToken = JWT.create()
                .withSubject(userEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return refreshToken;

    }


    public String getAccessHeader(HttpServletRequest request) {

        String header = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        return header;

    }

    public String getRefreshHeader(HttpServletRequest request) {

        String header = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);
        return header;

    }

    public String getAccessJwtWithoutPrefix(HttpServletRequest request) {

        String noPrefixAccessJwt = getAccessHeader(request).replace(JwtProperties.TOKEN_PREFIX,"");
        return noPrefixAccessJwt;

    }

    public String getRefreshJwtWithoutPrefix(HttpServletRequest request) {

        String noPrefixRefreshJwt = getRefreshHeader(request).replace(JwtProperties.TOKEN_PREFIX,"");
        return noPrefixRefreshJwt;

    }

    public DecodedJWT verifyToken(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
        return decodedJWT;

    }

    public boolean isExpired(String token) {

        try {

            verifyToken(token);
            return false;

        } catch (TokenExpiredException e) {

            return true;

        }
    }

    public String getClaim(String token) {

        String claim = verifyToken(token).getClaim("userEmail").asString();
        return claim;

    }

    public String createNewAccessToken(String userEmail) {

        User user = userRepository.findByUserEmail(userEmail).get();
        UserDTO userDTO = user.EntityToDTO();
        String userName = userDTO.getUserName();
        String accessToken = createAccessToken(userName, userEmail);
        return accessToken;

    }

    public boolean isNeedToUpdateRefreshToken(String token) {
        System.out.println("리프레시 토큰 갱신 필요한가요?");
        try {
            Date expiresAt = verifyToken(token).getExpiresAt();
            System.out.println("리프레시토큰 만료시간" + expiresAt);
            Date currentTime = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentTime);
            calendar.add(Calendar.MINUTE, 1);
            Date after7daysFromNow = calendar.getTime();
            System.out.println("캘린더 시간" + after7daysFromNow);

            if(expiresAt.before(after7daysFromNow)) {
                System.out.println("리프레시토큰 만료되기 1분전" + expiresAt.before(after7daysFromNow));
                return true;
            }
        } catch (TokenExpiredException e) {
            return true;
        }
        return false;
    }

    public ResponseEntity<?> renewAccessToken(HttpServletRequest request) {
        System.out.println("토큰 만료되서 리프레시 토큰으로 갱신중");
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();

        try {
            String noPrefixRefreshToken = getRefreshJwtWithoutPrefix(request);
            String userEmail = verifyToken(noPrefixRefreshToken).getSubject();

            if(!isExpired(noPrefixRefreshToken)) {
                String newAccessToken = createNewAccessToken(userEmail);
                returnMap.put("newAccessToken", JwtProperties.TOKEN_PREFIX + newAccessToken);
//                if(isNeedToUpdateRefreshToken(noPrefixRefreshToken)) {
//                    RefreshToken refreshToken = new RefreshToken();
//                    refreshToken.setUserName(userId);
//                    refreshToken.setRefreshToken(noPrefixRefreshToken);
//                    refreshTokenRepository.save(refreshToken);
//                    String newRefreshToken = createRefreshToken(userId);
//                    returnMap.put("newRefreshToken", JwtProperties.TOKEN_PREFIX + newRefreshToken);
//                    System.out.println("-======이건 리턴맵===========" + returnMap);
//                }
            }
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            returnMap.put("refresh", JwtErrors.EXPIRED_TOKEN);
            responseDTO.setItem(returnMap);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
