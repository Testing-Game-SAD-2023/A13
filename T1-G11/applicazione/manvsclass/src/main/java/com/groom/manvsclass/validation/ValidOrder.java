package com.groom.manvsclass.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrder {
    String message() default "I codici non sono progressivi o sequenziali.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}