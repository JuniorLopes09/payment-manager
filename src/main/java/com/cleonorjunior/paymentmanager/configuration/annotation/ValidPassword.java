package com.cleonorjunior.paymentmanager.configuration.annotation;

import com.cleonorjunior.paymentmanager.configuration.annotation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "{default.validPassword.valid.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}