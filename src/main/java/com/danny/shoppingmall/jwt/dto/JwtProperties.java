package com.danny.shoppingmall.jwt.dto;

public interface JwtProperties {
    String SECRET = "win";
    int ACCESS_EXPIRATION_TIME = 180000;
    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER_STRING = "AccessToken";
    String REFRESH_HEADER_STRING = "RefreshToken";
    int REFRESH_EXPIRATION_TIME = 3000000;
    String JWT_EXCEPTION = "Exception";
}
