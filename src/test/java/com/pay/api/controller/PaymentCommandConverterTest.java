package com.pay.api.controller;

import com.pay.api.exception.InvalidParameterException;
import com.pay.api.exception.InvalidPromotionTypeException;
import com.pay.api.helper.PaymentCommandConverter;
import com.pay.api.model.api.PaymentRequest;
import com.pay.api.model.api.PromotionRequest;
import com.pay.api.model.enums.PromotionType;
import com.pay.api.model.command.PaymentCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        PaymentCommand actual = paymentCommandConverter.convert(mockPaymentRequest);

        // then
        assertNotNull(actual);
        assertEquals(amount, actual.getAmount());
        assertEquals(amount, actual.getPromotionFinalPrice());
        assertFalse(actual.getIsPromotionPrice());
    }

    @Test
    void promotion_size_is_over_two() {
        // given
        Integer amount = 10000;
        Integer promotionAmount = 5000;
        Float promotionRatio = 0.5F;
        PromotionRequest mockPromotionRequest = getMockPromotionRequest(PromotionType.AMOUNT.getCode(), promotionAmount, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockPromotionRequest, mockPromotionRequest, mockPromotionRequest));

        // when & then
        assertThrows(InvalidParameterException.class, () -> paymentCommandConverter.convert(mockPaymentRequest));
    }

    @Test
    void invalid_promotion_type() {
        // given
        String invalidPromotionTypeCode = "test";
        Integer amount = 10000;
        Integer promotionAmount = 5000;
        Float promotionRatio = 0.5F;
        PromotionRequest mockPromotionRequest = getMockPromotionRequest(invalidPromotionTypeCode, promotionAmount, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockPromotionRequest));

        // when & then
        assertThrows(InvalidPromotionTypeException.class, () -> paymentCommandConverter.convert(mockPaymentRequest));
    }

    @Test
    void invalid_promotion_discount_amount_type() {
        // given
        Integer amount = 10000;
        Integer promotionAmount = 11000;
        Float promotionRatio = 0.5F;
        PromotionRequest mockPromotionRequest = getMockPromotionRequest(PromotionType.AMOUNT.getCode(), promotionAmount, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockPromotionRequest));

        // when & then
        assertThrows(InvalidParameterException.class, () -> paymentCommandConverter.convert(mockPaymentRequest));
    }

    @Test
    void promotional_price_is_negative() {
        // given
        Integer amount = 10000;
        Integer promotionAmount = 5000;
        Float promotionRatio = 51.0F;

        PromotionRequest mockAmountPromotionRequest = getMockPromotionRequest(PromotionType.AMOUNT.getCode(), promotionAmount, null);
        PromotionRequest mockRatioPromotionRequest = getMockPromotionRequest(PromotionType.RATIO.getCode(), null, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockAmountPromotionRequest, mockRatioPromotionRequest));

        // when & then
        assertThrows(InvalidParameterException.class, () -> paymentCommandConverter.convert(mockPaymentRequest));
    }

    @Test
    void success() {
        // given
        Integer amount = 10000;
        Integer promotionAmount = 3000;
        Float promotionRatio = 33.3F;
        // excepted price
        Integer exceptedPrice = amount - promotionAmount - ((int) (amount * (promotionRatio / 100)));

        PromotionRequest mockAmountPromotionRequest = getMockPromotionRequest(PromotionType.AMOUNT.getCode(), promotionAmount, null);
        PromotionRequest mockRatioPromotionRequest = getMockPromotionRequest(PromotionType.RATIO.getCode(), null, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockAmountPromotionRequest, mockRatioPromotionRequest));

        // when
        PaymentCommand actual = paymentCommandConverter.convert(mockPaymentRequest);
        assertEquals(amount, actual.getAmount());
        assertEquals(exceptedPrice, actual.getPromotionFinalPrice());
        assertTrue(actual.getIsPromotionPrice());
        assertNotNull(actual);
    }

    private PaymentRequest getMockPaymentRequest(Integer amount, List<PromotionRequest> promotions) {
        return new PaymentRequest(amount, "title", promotions);
    }

    private PromotionRequest getMockPromotionRequest(String promotionType, Integer promotionAmount, Float promotionRatio) {
        return new PromotionRequest(promotionType, "promotionTitleTest", promotionAmount, promotionRatio);
    }

}
