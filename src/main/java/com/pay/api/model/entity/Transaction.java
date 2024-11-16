package com.pay.api.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    // 트랜잭션 고유값
    @Id
    @Column(name = "tr_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계좌 고유값
    @Column(name = "account_seq")
    private Long accountSeq;

    // 할인 전 금액
    @Column(name = "amount")
    private Integer amount;

    // 할인 적용 이후 금액
    @Column(name = "payment_amount")
    private Integer paymentAmount;

    // 거래 제목
    @Column(name = "title")
    private String title;

    // 거래일시
    @Column(name = "registered")
    private LocalDateTime registeredDate;

}
