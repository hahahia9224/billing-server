package com.pay.api.exception;

public class InvalidParameterExceptionApi extends PayApiCustomException {
    public InvalidParameterExceptionApi(String message) {
        super(ErrorCode.INVALID_PARAMETER, message);
    }
}
