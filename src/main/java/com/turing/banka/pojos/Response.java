package com.turing.banka.pojos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class Response<T> {

    private HttpStatus status;
    private String message;
    private T data;
}
