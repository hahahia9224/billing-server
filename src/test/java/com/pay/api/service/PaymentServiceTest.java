package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentService paymentService;

    private final Long accountId = 1L;

    @Test
    void account_not_found_exception() {
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // then
        assertThrows(AccountNotFoundException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void amount_is_more_than_account_balance_exception() {
        Integer balance = 1000;
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void promotion_amount_more_than_account_balance_exception() {
        Integer balance = 1000;
        Integer amount = 2000;
        Integer promotionFinalPrice = 1500;
        Boolean isPromotionPrice = true;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice, isPromotionPrice);

        // 프로모션 적용하더라도 잔액이 부족한 경우
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));

    }

    private PaymentCommand getMockPaymentCommand(Integer amount, Integer promotionFinalPrice, Boolean isPromotionPrice) {
        String title = "test_title";
        return new PaymentCommand(amount, promotionFinalPrice, isPromotionPrice, title, new ArrayList<>());
    }

    private Account getMockAccount(Integer balance) {
        return new Account(accountId, LocalDateTime.now(), balance);
    }
}
