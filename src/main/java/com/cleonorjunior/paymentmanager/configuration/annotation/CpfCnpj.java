package com.cleonorjunior.paymentmanager.configuration.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = { CpfCnpjValidator.class })
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface CpfCnpj {

    String message() default "{default.cpfCnpj.valid.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}