package com.danny.shoppingmall.cart.repository;

import com.danny.shoppingmall.cart.entity.Cart;
import com.danny.shoppingmall.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {

//    @Query("select c from Cart c left join fetch c.products where c.user = :user")
//    List<Cart> findAllByUser(@Param("user") User user);

    @Query("select c from Cart c join fetch c.products where c.user = :user")
    List<Cart> findProductsByUser(User user);
}
