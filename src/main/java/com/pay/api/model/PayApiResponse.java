package com.pay.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pay.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayApiResponse {

    private final String resultCode;

    private final String resultMessage;

    private final Long transactionSeq;

    private final Integer balance;

    public static PayApiResponse errorFrom(ErrorCode errorCode, String resultMessage) {
        return new PayApiResponse(
                errorCode.getResultCode(),
                resultMessage,
                null,
                null
        );
    }

    public static PayApiResponse ok(Long transactionSeq, Integer balance) {
        return new PayApiResponse(
                "0000",
                "success",
                transactionSeq,
                balance
        );
    }
}
