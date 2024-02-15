package com.danny.shoppingmall.orders.service;

import com.danny.shoppingmall.orders.entity.Orders;
import com.danny.shoppingmall.orders.repository.OrdersRepository;
import com.danny.shoppingmall.products.dto.ProductsDTO;
import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.Address;
import com.danny.shoppingmall.user.entity.User;
import com.danny.shoppingmall.user.repository.AddressRepository;
import com.danny.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> saveOrders(UserDTO userDTO, ProductsDTO productsDTO) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();

        try {
            Orders orders = Orders.builder()
                    .user(userDTO.DTOToEntity())
                    .products(productsDTO.DTOToEntity())
                    .build();

            ordersRepository.save(orders);

            returnMap.put("save", "ok");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public ResponseEntity<?> saveOrder(UserDTO userDTO, Orders orders) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();
        try {
            Address address1 = Address.builder()
                    .zipcode(orders.getAddress().getZipcode())
                    .address(orders.getAddress().getAddress())
                    .addressDetail(orders.getAddress().getAddressDetail())
                    .build();

            String zipcode = orders.getAddress().getZipcode();
            String address = orders.getAddress().getAddress();
            String addressDetail = orders.getAddress().getAddressDetail();

            Optional<Address> addressCheck = addressRepository.findByZipcodeAndAddressAndAddressDetail(zipcode, address, addressDetail);
            if(addressCheck.isEmpty()) {

                addressRepository.save(address1);
            }

            if(orders.isSaveAsMyAddress()){
                User user = userRepository.findByUserEmail(userDTO.getUserEmail()).get();
                user.setAddress(address1);
                userRepository.save(user);
            }
            Orders orders1 = Orders.builder()
                    .user(userDTO.DTOToEntity())
                    .products(orders.getProducts())
                    .receiverName(orders.getReceiverName())
                    .receiverPhoneNum(orders.getReceiverPhoneNum())
                    .address(address1)
                    .saveAsMyAddress(orders.isSaveAsMyAddress())
                    .build();
            System.out.println("이건 오더1" + orders1);
            ordersRepository.save(orders1);
            System.out.println("그래서 저장 됐냐?");
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
}
