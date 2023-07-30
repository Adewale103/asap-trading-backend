package com.twinkles.asaptrading.dto.request;

import com.twinkles.asaptrading.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BuyCoinRequest {
    private String userId;
    private String paymentReferenceId;
    private String coinName;
    private String walletAddress;
    private BigDecimal totalAmount;
}
