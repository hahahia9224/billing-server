package com.pay.api.model.dto;

import com.pay.api.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private LocalDateTime registeredDate;
    private Long balance;

    public static AccountDto from(Account account) {
        return new AccountDto(
                account.getId(),
                account.getRegisteredDate(),
                account.getBalance());
    }
}
