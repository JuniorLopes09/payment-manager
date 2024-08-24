package com.cleonorjunior.paymentmanager.configuration.exception;

public class RegisterNotFoundException extends RuntimeException {

    public RegisterNotFoundException(Integer id) {
        super(String.format("Registro com id %d não encontrado.", id));
    }

}