package com.pay.api.controller;

import com.pay.api.helper.PaymentCommandConverter;
import com.pay.api.model.api.PayApiResponse;
import com.pay.api.model.api.PaymentRequest;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.dto.PaymentResultDto;
import com.pay.api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentCommandConverter paymentCommandConverter;

    @PostMapping("/v1/payment/account/{accountSeq}")
    public ResponseEntity<PayApiResponse> payment(@Valid @RequestBody PaymentRequest paymentRequest, @PathVariable Long accountSeq) {

        PaymentCommand paymentCommand = paymentCommandConverter.convert(paymentRequest);
        PaymentResultDto paymentResultDto = paymentService.payment(accountSeq, paymentCommand);

        return ResponseEntity.ok(PayApiResponse.ok(paymentResultDto.getTransactionSeq(), paymentResultDto.getBalance()));
    }

}
