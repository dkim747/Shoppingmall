package com.danny.shoppingmall.cart.entity;

import com.danny.shoppingmall.cart.dto.CartDTO;
import com.danny.shoppingmall.products.entity.Products;
import com.danny.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Products products;

    public CartDTO entityToDTO() {
        return CartDTO.builder()
                .id(this.id)
                .products(this.products)
                .build();
    }
}
