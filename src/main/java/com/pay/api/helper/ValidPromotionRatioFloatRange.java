package com.pay.api.helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PromotionRatioFloatRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPromotionRatioFloatRange {
    String message() default "invalid promotionRatio range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
