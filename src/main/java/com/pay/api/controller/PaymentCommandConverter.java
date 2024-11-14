package com.pay.api.controller;

import com.pay.api.exception.InvalidParameterException;
import com.pay.api.model.PaymentRequest;
import com.pay.api.model.PromotionRequest;
import com.pay.api.service.PaymentCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class PaymentCommandConverter {
    public PaymentCommand convert(PaymentRequest paymentRequest) {

        List<PromotionRequest> promotions = paymentRequest.getPromotions();

        if (CollectionUtils.isEmpty(promotions)) {
            return new PaymentCommand(
                    paymentRequest.getAmount(),
                    paymentRequest.getAmount(),
                    false,
                    paymentRequest.getTitle()
            );
        }

        if (promotions.size() > 2) {
            throw new InvalidParameterException("promotion count must be less than 2");
        }

        return null;
    }
}
