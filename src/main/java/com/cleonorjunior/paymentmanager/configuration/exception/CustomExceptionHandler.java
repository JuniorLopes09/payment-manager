package com.cleonorjunior.paymentmanager.configuration.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RegisterNotFoundException.class)
    public ResponseEntity<Object> handleRegisterNotFoundException(
            RegisterNotFoundException exception, WebRequest request) {

        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, uri, exception.getMessage());

        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(
            BusinessException exception, WebRequest request) {

        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getStatus(), uri, exception.getMessage());

        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map((error) -> error
                        .getDefaultMessage()
                        .replace("{field}", error.getField())
                        .replace("{value}", Optional.ofNullable(error.getRejectedValue()).map(Object::toString).orElse(""))
                )
                .collect(Collectors.toList());

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.BAD_REQUEST, uri, errors);

        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }
}