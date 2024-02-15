package com.danny.shoppingmall.products.entity;

import com.danny.shoppingmall.cart.entity.Cart;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private int productPrice;
    private String link;
    private String title;
    private String content;
    private String image;

    public ProductsDTO entityToDTO() {
        return ProductsDTO.builder()
                .id(this.id)
                .productName(this.productName)
                .productPrice(this.productPrice)
                .link(this.link)
                .content(this.content)
                .image(this.image)
                .build();
    }
}
