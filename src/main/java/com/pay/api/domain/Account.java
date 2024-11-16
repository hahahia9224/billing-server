package com.pay.api.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    // 계좌 고유값
    @Id
    @Column(name = "account_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계좌 개설 날짜
    @Column(name = "registered")
    private LocalDateTime registeredDate;

    // 계좌 잔액 금액
    @Column(name = "balance")
    private Integer balance;
}
