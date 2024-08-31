package com.shimady.tracker.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {

    String message () default "{phoneNumber.invalid";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
