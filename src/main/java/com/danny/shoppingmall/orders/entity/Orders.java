package com.danny.shoppingmall.orders.entity;

import com.danny.shoppingmall.products.entity.Products;
import com.danny.shoppingmall.user.entity.Address;
import com.danny.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Products products;
    private String receiverName;
    private String receiverPhoneNum;
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
    private boolean saveAsMyAddress;
    private int countOfOrders;
}
