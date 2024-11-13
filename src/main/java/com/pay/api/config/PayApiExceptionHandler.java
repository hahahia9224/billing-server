package com.pay.api.config;

import com.pay.api.exception.ErrorCode;
import com.pay.api.exception.PayApiCustomException;
import com.pay.api.model.PayApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PayApiExceptionHandler {

    @ExceptionHandler(PayApiCustomException.class)
    public ResponseEntity<PayApiResponse> handlePayApiException(Exception e) {

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        if (e instanceof PayApiCustomException payApiCustomException) {
            errorCode = payApiCustomException.getErrorCode();
        }

        // 별도로 주입 받은 getMessage 가 없다면, ErrorCode 내 resultMessage 사용
        String errorMessage = (e.getMessage() != null) ? e.getMessage() : errorCode.getResultMessage();

        PayApiResponse errorResponse = PayApiResponse.errorFrom(errorCode, errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
