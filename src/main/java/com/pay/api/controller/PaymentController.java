package com.pay.api.controller;

import com.pay.api.model.api.PayApiResponse;
import com.pay.api.model.api.PaymentRequest;
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

    @PostMapping("/v1/payment/account/{accountSeq}")
    public ResponseEntity<PayApiResponse> payment(@Valid @RequestBody PaymentRequest paymentRequest, @PathVariable Long accountSeq) {
        return ResponseEntity.ok(PayApiResponse.ok(1L, 991900));
    }

}
