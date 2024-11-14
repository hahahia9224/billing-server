package com.pay.api.helper;

import com.pay.api.exception.InvalidPromotionTypeException;
import com.pay.api.model.enums.PromotionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PromotionTypeValidator implements ConstraintValidator<ValidPromotionType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            PromotionType.from(value);
            return true;
        } catch (InvalidPromotionTypeException e) {
            return false;
        }
    }
}
