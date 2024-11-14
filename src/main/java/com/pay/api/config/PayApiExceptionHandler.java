package com.pay.api.config;

import com.pay.api.exception.ErrorCode;
import com.pay.api.exception.PayApiCustomException;
import com.pay.api.model.PayApiResponse;
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
        ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;

        String message = (fieldError == null) ? errorCode.getResultMessage() : fieldError.getDefaultMessage();
        PayApiResponse errorResponse = PayApiResponse.errorFrom(errorCode, message);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<PayApiResponse> handlePayApiException(Exception e) {

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        if (e instanceof PayApiCustomException payApiCustomException) {
            errorCode = payApiCustomException.getErrorCode();
        }

        String errorMessage = (e.getMessage() != null) ? e.getMessage() : errorCode.getResultMessage();

        PayApiResponse errorResponse = PayApiResponse.errorFrom(errorCode, errorMessage);
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
