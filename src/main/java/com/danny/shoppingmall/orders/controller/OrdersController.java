package com.danny.shoppingmall.orders.controller;

import com.danny.shoppingmall.orders.entity.Orders;
import com.danny.shoppingmall.orders.service.OrdersService;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.security.PrincipalDetails;
import com.danny.shoppingmall.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/order")
    public ResponseEntity<?> orders(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ProductsDTO productsDTO) {

        UserDTO userDTO = principalDetails.getUserDTO();

        return ordersService.saveOrders(userDTO, productsDTO);
    }

    @PostMapping("/getorder")
    public ResponseEntity<?> getOrder(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody Orders orders) {
        System.out.println("이건 받은 오더" + orders);
        UserDTO userDTO = principalDetails.getUserDTO();
        return ordersService.saveOrder(userDTO, orders);
    }

}
