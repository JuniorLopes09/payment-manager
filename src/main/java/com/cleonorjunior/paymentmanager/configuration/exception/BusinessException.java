package com.cleonorjunior.paymentmanager.configuration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

    public BusinessException(String message) {
        super(message);

    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}