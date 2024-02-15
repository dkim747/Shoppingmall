package com.danny.shoppingmall.orders.dto;

import com.danny.shoppingmall.products.entity.Products;
import com.danny.shoppingmall.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDTO {

    private Long id;
    private User user;
    private Products products;
    private int countOfProducts;
}
