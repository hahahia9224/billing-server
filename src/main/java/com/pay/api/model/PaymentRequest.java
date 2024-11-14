package com.pay.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {

    @NotNull(message = "amount is required")
    @Min(value = 1, message = "amount is positive (non zero)")
    private Integer amount;

    @NotNull(message = "title is required")
    @Size(max = 255, message = "title length is must be 255 characters or less")
    private String title;

    @Valid
    private List<PromotionRequest> promotions;

}
