package com.pay.api.service;

import com.pay.api.domain.Account;
import com.pay.api.exception.AccountNotFoundException;
import com.pay.api.exception.AmountNotEnoughException;
import com.pay.api.model.command.PaymentCommand;
import com.pay.api.model.dto.AccountDto;
import com.pay.api.repository.AccountRepository;
import com.pay.api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentResult payment(Long accountId, PaymentCommand paymentCommand) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }

        AccountDto accountDto = AccountDto.from(account.get());

        if (paymentCommand.getPromotionFinalPrice() > accountDto.getBalance()) {
            throw new AmountNotEnoughException();
        }

        return new PaymentResult(1L, 8500);
    }
}
