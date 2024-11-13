package com.linecorp.linepay.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {

    private Integer amount;

    private String title;

    private List<PromotionRequest> promotions;
}
