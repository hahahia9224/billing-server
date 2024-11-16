package com.pay.api.controller;

import com.pay.api.config.PayApiExceptionHandler;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.exception.ErrorCode;
import com.pay.api.helper.PaymentCommandConverter;
import com.pay.api.model.api.PaymentRequest;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.dto.PaymentResultDto;
import com.pay.api.service.PaymentService;
import com.pay.api.service.PaymentTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentCommandConverter paymentCommandConverter;

    @InjectMocks
    private PaymentController paymentController;

    private String uriPath = "/v1/payment/account/{accountSeq}";

    Long accountSeq = 1L;
    Integer amount = 2000;
    Integer promotionFinalPrice = 1500;
    Boolean isPromotionPrice = true;
    Integer balance = 1000;
    Long transactionSeq = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new PayApiExceptionHandler())
                .build();
    }

    @Test
    void test_payment_post_success() throws Exception {
        // Response Example 1 - payment success

        // given
        PaymentCommand mockPaymentCommand = PaymentTestUtils.getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);
        PaymentResultDto paymentResultDto = new PaymentResultDto(transactionSeq, balance);

        // when
        when(paymentCommandConverter.convert(any(PaymentRequest.class))).thenReturn(mockPaymentCommand);
        when(paymentService.payment(anyLong(), any(PaymentCommand.class))).thenReturn(paymentResultDto);

        // then
        mockMvc.perform(post(uriPath, accountSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonContent("payment-request.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("0000"))
                .andExpect(jsonPath("$.resultMessage").value("success"))
                .andExpect(jsonPath("$.transactionSeq").value(transactionSeq))
                .andExpect(jsonPath("$.balance").value(balance));
    }

    @Test
    void test_payment_post_title_is_required_failure() throws Exception {
        // Response Example 2 - Invalid parameter

        // given
        PaymentCommand mockPaymentCommand = PaymentTestUtils.getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);
        PaymentResultDto paymentResultDto = new PaymentResultDto(transactionSeq, balance);

        // when
        when(paymentCommandConverter.convert(any(PaymentRequest.class))).thenReturn(mockPaymentCommand);
        when(paymentService.payment(anyLong(), any(PaymentCommand.class))).thenReturn(paymentResultDto);

        // then
        mockMvc.perform(post(uriPath, accountSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonContent("payment-request-title-not-exist.json")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(ErrorCode.INVALID_PARAMETER.getResultCode()))
                .andExpect(jsonPath("$.resultMessage").value("title is required"));
    }

    @Test
    void test_payment_post_promotion_ration_invalid() throws Exception {
        // Response Example 2 - Invalid parameter (promotionRatio range is 0 ~ 100)

        // given
        PaymentCommand mockPaymentCommand = PaymentTestUtils.getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);
        PaymentResultDto paymentResultDto = new PaymentResultDto(transactionSeq, balance);

        // when
        when(paymentCommandConverter.convert(any(PaymentRequest.class))).thenReturn(mockPaymentCommand);
        when(paymentService.payment(anyLong(), any(PaymentCommand.class))).thenReturn(paymentResultDto);

        // then
        mockMvc.perform(post(uriPath, accountSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonContent("payment-request-invalid-ratio.json")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(ErrorCode.INVALID_PARAMETER.getResultCode()))
                .andExpect(jsonPath("$.resultMessage").value("promotionRatio range is 0 ~ 100"));
    }

    @Test
    void test_payment_post_amount_not_enough() throws Exception {
        // Response Example 3 - 잔액부족

        // given
        PaymentCommand mockPaymentCommand = PaymentTestUtils.getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(paymentCommandConverter.convert(any(PaymentRequest.class))).thenReturn(mockPaymentCommand);
        when(paymentService.payment(anyLong(), any(PaymentCommand.class))).thenThrow(new AmountNotEnoughException());

        // then
        mockMvc.perform(post(uriPath, accountSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonContent("payment-request.json")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.resultCode").value(ErrorCode.AMOUNT_NOT_ENOUGH.getResultCode()))
                .andExpect(jsonPath("$.resultMessage").value(ErrorCode.AMOUNT_NOT_ENOUGH.getResultMessage()));
    }

    @Test
    void test_payment_post_not_pay_api_exception_case() throws Exception {
        // Response Example 4 - 기타 오류

        // given
        PaymentCommand mockPaymentCommand = PaymentTestUtils.getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);
        String errorMessage = "server error";

        // when
        when(paymentCommandConverter.convert(any(PaymentRequest.class))).thenReturn(mockPaymentCommand);
        when(paymentService.payment(anyLong(), any(PaymentCommand.class))).thenThrow(new RuntimeException(errorMessage));

        // then
        mockMvc.perform(post(uriPath, accountSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJsonContent("payment-request.json")))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.resultCode").value(ErrorCode.INTERNAL_SERVER_ERROR.getResultCode()))
                .andExpect(jsonPath("$.resultMessage").value(errorMessage));
    }

    private String readJsonContent(String fileName) throws IOException {
        String basePath = "src/test/resources/json/";
        return Files.readString(Paths.get(basePath + fileName));
    }
}
