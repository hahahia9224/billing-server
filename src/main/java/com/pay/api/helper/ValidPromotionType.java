package com.pay.api.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PromotionTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPromotionType {
    String message() default "invalid promotion type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
