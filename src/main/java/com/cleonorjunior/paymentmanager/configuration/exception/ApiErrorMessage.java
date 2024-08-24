package com.cleonorjunior.paymentmanager.configuration.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiErrorMessage {

    private String path;

    private HttpStatus status;

    private List<String> errors;

    public ApiErrorMessage(HttpStatus status, String path, List<String> errors) {
        super();
        this.status = status;
        this.path = path;
        this.errors = errors;
    }

    public ApiErrorMessage(HttpStatus status, String path, String error) {
        super();
        this.status = status;
        this.path = path;
        this.errors = Collections.singletonList(error);
    }

}