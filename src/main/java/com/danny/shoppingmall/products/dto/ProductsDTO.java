package com.danny.shoppingmall.products.dto;

import com.danny.shoppingmall.products.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsDTO {

    private Long id;
    private String productName;
    private int productPrice;
    private String link;
    private String content;
    private String image;

    public Products DTOToEntity() {
        return Products.builder()
                .id(this.id)
                .productName(this.productName)
                .productPrice(this.productPrice)
                .link(this.link)
                .content(this.content)
                .image(this.image)
                .build();
    }
}
