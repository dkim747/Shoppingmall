package com.danny.shoppingmall.user.service;

import com.danny.shoppingmall.user.dto.ResponseDTO;
import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.entity.User;
import com.danny.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<?> signUp(UserDTO userDTO) {

        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        Map<String, String> returnMap = new HashMap<>();

        try {
            if(checkUser(userDTO.getUserEmail())) {
                returnMap.put("msg", "save fail");
            } else {
                userDTO.setUserPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()));
                userDTO.setUserRoles("ROLE_USER");
                User user = userDTO.DTOToEntity();
                userRepository.save(user);
                returnMap.put("msg", "save ok");
            }
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    public boolean checkUser(String userEmail) {
        return userRepository.findByUserEmail(userEmail).isPresent() ? true : false;
    }
}
