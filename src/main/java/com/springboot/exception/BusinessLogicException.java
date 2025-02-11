package com.springboot.exception;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }



}
