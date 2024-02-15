package com.danny.shoppingmall.user.entity;

import com.danny.shoppingmall.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String userPw;
    private String userName;
    private int userPhoneNum;
    private String userRoles;
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;

    public UserDTO EntityToDTO() {
        return UserDTO.builder()
                .id(this.id)
                .userEmail(this.userEmail)
                .userPw(this.userPw)
                .userName(this.userName)
                .userPhoneNum(this.userPhoneNum)
                .userRoles(this.userRoles)
                .build();
    }
}
