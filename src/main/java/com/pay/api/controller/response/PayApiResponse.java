package com.pay.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pay.api.exception.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayApiResponse {

    private final String resultCode;

    private final String resultMessage;

    private final Long transactionSeq;

    private final Integer balance;

    public static PayApiResponse errorFrom(ResponseCode responseCode, String resultMessage) {
        return new PayApiResponse(
                responseCode.getResultCode(),
                resultMessage,
                null,
                null
        );
    }

    public static PayApiResponse ok(Long transactionSeq, Integer balance) {
        return new PayApiResponse(
                ResponseCode.SUCCESS.getResultCode(),
                ResponseCode.SUCCESS.getResultMessage(),
                transactionSeq,
                balance
        );
    }
}
