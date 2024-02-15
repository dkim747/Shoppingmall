package com.danny.shoppingmall.jwt.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String userEmail;
    private String userPw;
}
