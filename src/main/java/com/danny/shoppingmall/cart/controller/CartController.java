package com.danny.shoppingmall.cart.controller;

import com.danny.shoppingmall.cart.service.CartService;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.security.PrincipalDetails;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<?> addCart(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ProductsDTO productsDTO) {

        UserDTO userDTO = principalDetails.getUserDTO();
        System.out.println("addCart 한번 실행");

        return cartService.addCart(userDTO, productsDTO);
    }

    @GetMapping("/get-cart")
    public ResponseEntity<?> getCart(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("응답은 받니?????????????");
        System.out.println(principalDetails.getUsername());
        UserDTO userDTO = principalDetails.getUserDTO();
        System.out.println("이건 받아온 userDTO" + userDTO);
        return cartService.getCart(userDTO);
    }
}
