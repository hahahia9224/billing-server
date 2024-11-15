package com.pay.api.model.command;

import com.pay.api.controller.request.PaymentRequest;
import com.pay.api.controller.request.PromotionRequest;
import com.pay.api.exception.InvalidParameterException;
import com.pay.api.exception.InvalidPromotionTypeException;
import com.pay.api.model.entity.PromotionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.pay.api.service.PaymentTestUtils.getMockPaymentRequest;
import static com.pay.api.service.PaymentTestUtils.getMockPromotionRequest;
import static org.junit.jupiter.api.Assertions.*;

class PaymentCommandTest {

    @Test
    void not_promotion_case() {
        // given
        Integer amount = 10000;
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of());

        // when
        PaymentCommand actual = PaymentCommand.convert(mockPaymentRequest);

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
        assertThrows(InvalidParameterException.class, () -> PaymentCommand.convert(mockPaymentRequest));
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
        assertThrows(InvalidPromotionTypeException.class, () -> PaymentCommand.convert(mockPaymentRequest));
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
        assertThrows(InvalidParameterException.class, () -> PaymentCommand.convert(mockPaymentRequest));
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
        assertThrows(InvalidParameterException.class, () -> PaymentCommand.convert(mockPaymentRequest));
    }

    @Test
    void success() {
        // given
        Integer amount = 10000;
        Integer promotionAmount = 90;
        Float promotionRatio = 10F;
        Integer exceptedPrice = 8910;

        PromotionRequest mockAmountPromotionRequest = getMockPromotionRequest(PromotionType.AMOUNT.getCode(), promotionAmount, null);
        PromotionRequest mockRatioPromotionRequest = getMockPromotionRequest(PromotionType.RATIO.getCode(), null, promotionRatio);
        PaymentRequest mockPaymentRequest = getMockPaymentRequest(amount, List.of(mockRatioPromotionRequest, mockAmountPromotionRequest));

        // when
        PaymentCommand actual = PaymentCommand.convert(mockPaymentRequest);

        // then
        assertEquals(amount, actual.getAmount());
        assertEquals(exceptedPrice, actual.getPromotionFinalPrice());
        assertTrue(actual.getIsPromotionPrice());
        assertNotNull(actual);
    }


}
