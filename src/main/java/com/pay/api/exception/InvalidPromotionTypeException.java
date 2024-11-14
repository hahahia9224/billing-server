package com.pay.api.exception;

public class InvalidPromotionTypeException extends PayApiCustomException {
    public InvalidPromotionTypeException() {
        super(ErrorCode.INVALID_PROMOTION_TYPE);
    }
}
