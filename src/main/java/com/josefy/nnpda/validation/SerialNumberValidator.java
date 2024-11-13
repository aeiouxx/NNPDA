package com.josefy.nnpda.validation;

import com.josefy.nnpda.annotation.SerialNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SerialNumberValidator implements ConstraintValidator<SerialNumber, String> {

    private boolean optional;

    @Override
    public void initialize(SerialNumber constraintAnnotation) {
        this.optional = constraintAnnotation.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return optional;
        }
        return value.matches("[0-9A-Fa-f]{12}");
    }
}
