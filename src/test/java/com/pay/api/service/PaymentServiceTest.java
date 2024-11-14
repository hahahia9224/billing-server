package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        Long amount = 2000L;
        Long promotionFinalPrice = 1500L;

        // given
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // then
        assertThrows(AccountNotFoundException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void amount_is_more_than_account_balance_exception() {
        Long balance = 1000L;
        Long amount = 2000L;
        Long promotionFinalPrice = 1500L;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice);

        // when
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));
    }

    @Test
    void promotion_amount_more_than_account_balance_exception() {
        Long balance = 1000L;
        Long amount = 2000L;
        Long promotionFinalPrice = 1500L;

        // given
        Account mockAccount = getMockAccount(balance);
        PaymentCommand mockPayCommand = getMockPaymentCommand(amount, promotionFinalPrice);

        // 프로모션 적용하더라도 잔액이 부족한 경우
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        // then
        assertThrows(AmountNotEnoughException.class, () -> paymentService.payment(accountId, mockPayCommand));

    }

    private PaymentCommand getMockPaymentCommand(Long amount, Long promotionFinalPrice) {
        return new PaymentCommand(amount, promotionFinalPrice);
    }

    private Account getMockAccount(Long balance) {
        return new Account(accountId, LocalDateTime.now(), balance);
    }
}
