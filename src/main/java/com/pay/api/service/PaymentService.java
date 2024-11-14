package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.dto.AccountDto;
import com.pay.api.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {
    private final AccountRepository accountRepository;

    public void payment(Long accountSeq, PaymentCommand paymentCommand) {
        Optional<Account> account = accountRepository.findById(accountSeq);
        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }

        AccountDto accountDto = AccountDto.from(account.get());

        if (paymentCommand.getPromotionFinalPrice() > accountDto.getBalance()) {
            throw new AmountNotEnoughException();
        }
    }
}
