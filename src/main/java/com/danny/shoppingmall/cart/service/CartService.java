package com.danny.shoppingmall.cart.service;

import com.danny.shoppingmall.cart.dto.CartDTO;
import com.danny.shoppingmall.cart.entity.Cart;
import com.danny.shoppingmall.cart.repository.CartRepository;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.products.entity.Products;
import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.User;
import com.danny.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public ResponseEntity<?> addCart(UserDTO userDTO, ProductsDTO productsDTO) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();
        try {
            User user = userDTO.DTOToEntity();
            System.out.println("요청보낸 유저객체" + user);
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProducts(productsDTO.DTOToEntity());
            cartRepository.save(cart);
            System.out.println("카트에 저장 됌");
            returnMap.put("save", "ok");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> getCart(UserDTO userDTO) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        Map<String, Object> returnMap = new HashMap<>();
        try {
            User user = userDTO.DTOToEntity();
            System.out.println("유저가 없나?" + user);
            List<Cart> cartList = cartRepository.findProductsByUser(user);
            List<CartDTO> cartDTOList = new ArrayList<>();
            for(Cart cart : cartList) {
                Long id = cart.getId();
                Products products = cart.getProducts();
                CartDTO cartDTO = CartDTO.builder()
                        .id(id)
                        .products(products)
                        .build();
                cartDTOList.add(cartDTO);
            }
            returnMap.put("cart", cartDTOList);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
