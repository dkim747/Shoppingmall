package com.danny.shoppingmall.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@Data
@RedisHash(value = "refreshToken")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String userEmail;

    private String refreshToken;
}
