package com.pay.api.model.entity;

import com.pay.api.exception.InvalidPromotionTypeException;
import lombok.Getter;

@Getter
public enum PromotionType {
    AMOUNT("amount"), // 금액 기반 할인
    RATIO("ratio"); // 비율 기반 할인

    private final String code;

    PromotionType(String code) {
        this.code = code;
    }

    public static PromotionType from(String code) {
        for (PromotionType type : PromotionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new InvalidPromotionTypeException();
    }
}
