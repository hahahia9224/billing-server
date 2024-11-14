package com.pay.api.controller;

import com.pay.api.exception.InvalidParameterException;
import com.pay.api.model.PayApiResponse;
import com.pay.api.model.PaymentRequest;
import com.pay.api.model.PromotionRequest;
import com.pay.api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/v1/payment/account/{accountSeq}")
    public ResponseEntity<PayApiResponse> payment(@Valid @RequestBody PaymentRequest paymentRequest, @PathVariable Long accountSeq) {

        List<PromotionRequest> promotions = paymentRequest.getPromotions();
        validatePromotions(promotions);
        validatePromotionAmount(paymentRequest.getAmount(), promotions);
        return ResponseEntity.ok(PayApiResponse.ok(1L, 991900));
    }

    private void validatePromotions(List<PromotionRequest> promotions) {
        if (promotions != null && promotions.size() > 2) {
            throw new InvalidParameterException("promotion count must be less than 2");
        }
    }

    private void validatePromotionAmount(Integer amount, List<PromotionRequest> promotions) {
        if (promotions == null || promotions.isEmpty()) return;

        promotions.stream()
                .filter(promotion -> "amount".equals(promotion.getPromotionType()))
                .forEach(promotion -> {
                    Integer promotionAmount = promotion.getPromotionAmount();
                    if (promotionAmount == null) {
                        throw new InvalidParameterException("promotionAmount is required(if promotion type is amount)");
                    }
                    if (promotionAmount < 1 || promotionAmount >= amount) {
                        throw new InvalidParameterException("promotion amount range is 1 ~ amount");
                    }
                });
    }
}
