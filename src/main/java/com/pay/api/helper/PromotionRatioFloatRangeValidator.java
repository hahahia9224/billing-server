package com.pay.api.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PromotionRatioFloatRangeValidator implements ConstraintValidator<ValidPromotionRatioFloatRange, Float> {

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        // Not Required
        if (value == null) {
            return true;
        }
        return value > 0 && value <= 100;
    }
}