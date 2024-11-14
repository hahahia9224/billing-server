package com.pay.api.controller;

import com.pay.api.exception.InvalidParameterException;
import com.pay.api.model.PaymentRequest;
import com.pay.api.model.PromotionRequest;
import com.pay.api.service.PaymentCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PaymentCommandConverterTest {

    @InjectMocks
    private PaymentCommandConverter paymentCommandConverter;

    @Test
    void not_promotion_case() {
        // given
        Integer amount = 10000;
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of());

        // when
        PaymentCommand result = paymentCommandConverter.convert(mockPaymentRequest);

        // then
        assertNotNull(result);
    }

    @Test
    void promotion_size_is_over_two() {
        // given
        String invalidPromotionTypeCode = "test";
        Integer amount = 10000;
        Integer promotionAmount = 5000;
        Float promotionRatio = 0.5F;
        PromotionRequest mockPromotionRequest = getMockPromotionRequest(invalidPromotionTypeCode, promotionAmount, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockPromotionRequest, mockPromotionRequest, mockPromotionRequest));

        // when & then
        assertThrows(InvalidParameterException.class, () -> paymentCommandConverter.convert(mockPaymentRequest));
    }

    private PaymentRequest getMockPaymentRequest(Integer amount, List<PromotionRequest> promotions) {
        return new PaymentRequest(amount, "title", promotions);
    }

    private PromotionRequest getMockPromotionRequest(String promotionType, Integer promotionAmount, Float promotionRatio) {
        return new PromotionRequest(promotionType, "promotionTitleTest", promotionAmount, promotionRatio);
    }

}