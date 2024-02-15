package com.danny.shoppingmall.cart.dto;

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
public class CartDTO {

    private long id;

    private Products products;

}
