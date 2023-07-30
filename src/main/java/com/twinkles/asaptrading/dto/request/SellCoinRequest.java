package com.twinkles.asaptrading.dto.request;

import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SellCoinRequest {
    private String userId;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountName;
    private String coinName;
    private BigDecimal totalAmount;
    private BigDecimal coinQuantity;
}
