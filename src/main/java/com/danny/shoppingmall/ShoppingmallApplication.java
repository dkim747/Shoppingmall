package com.danny.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.danny.shoppingmall.user.repository", "com.danny.shoppingmall.products.repository", "com.danny.shoppingmall.orders.repository", "com.danny.shoppingmall.cart.repository"})
public class ShoppingmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingmallApplication.class, args);
    }

}
