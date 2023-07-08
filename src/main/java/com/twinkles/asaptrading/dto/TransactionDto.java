package com.twinkles.asaptrading.dto;

import com.twinkles.asaptrading.entity.AccountDetails;
import com.twinkles.asaptrading.enums.TransactionStatus;
import com.twinkles.asaptrading.enums.TransactionType;
import lombok.*;


import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransactionDto {
    private Long id;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private BigDecimal amount;
    private String coinName;
    private AccountDetails accountDetails;
    private String walletAddress;
}
