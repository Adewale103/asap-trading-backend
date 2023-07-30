package com.twinkles.asaptrading.dto.request;

import com.twinkles.asaptrading.enums.CoinType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddCoinRequest {
    private String name;
    private BigDecimal unitPrice;
}
