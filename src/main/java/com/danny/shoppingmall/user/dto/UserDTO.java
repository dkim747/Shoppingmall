package com.danny.shoppingmall.user.dto;

import com.danny.shoppingmall.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long id;
    private String userEmail;
    private String userPw;
    private String userName;
    private int userPhoneNum;
    private String userRoles;

    public List<String> getRoleList() {
        if(this.userRoles.length() > 0) {
            return Arrays.asList(this.userRoles.split(","));
        }
        return new ArrayList<>();
    }

    public User DTOToEntity() {
        return User.builder()
                .id(this.id)
                .userEmail(this.userEmail)
                .userPw(this.userPw)
                .userName(this.userName)
                .userPhoneNum(this.userPhoneNum)
                .userRoles(this.userRoles)
                .build();
    }
}
