package com.cleonorjunior.paymentmanager.configuration.annotation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EnumValidatorTest {

    private EnumValidator validator;

    private enum TestEnum {
        VALID_VALUE
    }

    @BeforeEach
    public void setUp() {
        validator = new EnumValidator();
        ValidEnum annotation = mock(ValidEnum.class);
        Mockito.when(annotation.enumClass()).thenAnswer(invocation -> TestEnum.class);
        validator.initialize(annotation);
    }

    @Test
    public void testIsValid_withValidValue() {
        boolean isValid = validator.isValid("VALID_VALUE", mock(ConstraintValidatorContext.class));
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_withInvalidValue() {
        boolean isValid = validator.isValid("INVALID_VALUE", mock(ConstraintValidatorContext.class));
        assertFalse(isValid);
    }

    @Test
    public void testIsValid_withNullValue() {
        boolean isValid = validator.isValid(null, mock(ConstraintValidatorContext.class));
        assertTrue(isValid);
    }
}
