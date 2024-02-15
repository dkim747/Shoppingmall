package com.danny.shoppingmall.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO<T> {
    private T item;
    private List<T> items;
    private String errorMessage;
    private int statusCode;
}
