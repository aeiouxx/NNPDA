package com.josefy.nnpda.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SerialNumberValidator.class)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerialNumber {
    String message() default "Serial number must be exactly 12 hexadecimal digits.";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
