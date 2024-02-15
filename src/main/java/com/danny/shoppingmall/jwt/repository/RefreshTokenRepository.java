package com.danny.shoppingmall.jwt.repository;

import com.danny.shoppingmall.jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByUserName(String userId);

    RefreshToken findByRefreshToken(RefreshToken refreshToken1);
}
