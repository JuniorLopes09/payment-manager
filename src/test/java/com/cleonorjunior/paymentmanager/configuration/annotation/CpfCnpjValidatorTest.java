package com.cleonorjunior.paymentmanager.configuration.annotation;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfCnpjValidatorTest {

    private CpfCnpjValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new CpfCnpjValidator();
        context = new ConstraintValidatorContextImpl(null, null, null, null, null, null);
    }

    @Test
    void testValidCpf() {
        String validCpf = "12345678909";
        assertTrue(validator.isValid(validCpf, context));
    }

    @Test
    void testInvalidCpf() {
        String invalidCpf = "12345678900";
        assertFalse(validator.isValid(invalidCpf, context));
    }

    @Test
    void testValidCnpj() {
        String validCnpj = "12345678000195";
        assertTrue(validator.isValid(validCnpj, context));
    }

    @Test
    void testInvalidCnpj() {
        String invalidCnpj = "12345678000100";
        assertFalse(validator.isValid(invalidCnpj, context));
    }

    @Test
    void testNullValid() {
        assertTrue(validator.isValid(null, context));
    }
    @Test
    void testInitialize() {
        validator.initialize(null);
    }
}