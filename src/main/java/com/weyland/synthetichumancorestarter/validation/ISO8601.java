package com.weyland.synthetichumancorestarter.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ISO8601Validator.class)
public @interface ISO8601 {
    String message() default "must be in ISO 8601 format (YYYY-MM-DDThh:mm:ss)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
