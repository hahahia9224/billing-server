package com.pay.api.exception;

public class InvalidPromotionTypeException extends PayApiCustomException {
    public InvalidPromotionTypeException() {
        super(ResponseCode.INVALID_PROMOTION_TYPE);
    }
}
