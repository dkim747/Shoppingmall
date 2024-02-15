package com.danny.shoppingmall.products.controller;

import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.products.service.ProductsService;
import com.danny.shoppingmall.security.PrincipalDetails;
import com.danny.shoppingmall.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/get-products")
    public ResponseEntity<?> getProductList() {

        return productsService.getProductList();

    }

    @PostMapping("/makeProducts")
    public ResponseEntity<?> makeProducts(@RequestBody ProductsDTO productsDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("받아온 링크" + productsDTO);
        System.out.println("요청한 사용자" + principalDetails.getUserDTO());
        UserDTO userDTO = principalDetails.getUserDTO();
        return productsService.makeProducts(productsDTO, userDTO);
    }

    @GetMapping("/get-product")
    public ResponseEntity<?> getProduct(@RequestParam String productId) {
        System.out.println("리퀘스트 받아 오나???" + productId);
        long id = Long.parseLong(productId);
        return productsService.getProduct(id);
    }
}
