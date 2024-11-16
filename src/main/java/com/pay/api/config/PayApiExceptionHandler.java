package com.pay.api.config;

import com.pay.api.controller.response.PayApiResponse;
import com.pay.api.exception.PayApiCustomException;
import com.pay.api.exception.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PayApiExceptionHandler {

    // Request Validation 용 ExceptionHandler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PayApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        ResponseCode responseCode = ResponseCode.INVALID_PARAMETER;

        String message = (fieldError == null) ? responseCode.getResultMessage() : fieldError.getDefaultMessage();
        PayApiResponse errorResponse = PayApiResponse.errorFrom(responseCode, message);
        return new ResponseEntity<>(errorResponse, responseCode.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<PayApiResponse> handlePayApiException(Exception e) {

        // PayApiCustomException 으로 정의된 케이스 가 아닌, 기타 오류 처리
        ResponseCode responseCode = ResponseCode.INTERNAL_SERVER_ERROR;

        if (e instanceof PayApiCustomException payApiCustomException) {
            responseCode = payApiCustomException.getResponseCode();
        }

        // errorMessage 정의 (PayApiCustomException 가 아닌 경우, e.getMessage() 를 사용)
        String errorMessage = (e.getMessage() != null) ? e.getMessage() : responseCode.getResultMessage();

        PayApiResponse errorResponse = PayApiResponse.errorFrom(responseCode, errorMessage);
        return new ResponseEntity<>(errorResponse, responseCode.getStatus());
    }

}
