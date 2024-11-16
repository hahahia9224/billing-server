package com.pay.api.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequest {

    // 결제 금액
    @NotNull(message = "amount is required")
    @Min(value = 1, message = "amount is positive (non zero)")
    private Integer amount;

    // 결제 제목
    @NotNull(message = "title is required")
    @Size(max = 255, message = "title length is must be 255 characters or less")
    private String title;

    // 프로모션 정보 리스트
    @Valid
    private List<PromotionRequest> promotions;
}
