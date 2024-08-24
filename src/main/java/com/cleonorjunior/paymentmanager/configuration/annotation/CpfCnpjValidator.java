package com.cleonorjunior.paymentmanager.configuration.annotation;

import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public void initialize(CpfCnpj constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpfCnpj, ConstraintValidatorContext context) {
        if (cpfCnpj == null) {
            return true;
        }

        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.initialize(null);

        CNPJValidator cnpjValidator = new CNPJValidator();
        cnpjValidator.initialize(null);

        boolean isCpfValid = cpfCnpj.length() == 11 && cpfValidator.isValid(cpfCnpj, context);
        boolean isCnpjValid = cpfCnpj.length() == 14 && cnpjValidator.isValid(cpfCnpj, context);

        return isCpfValid || isCnpjValid;
    }
}