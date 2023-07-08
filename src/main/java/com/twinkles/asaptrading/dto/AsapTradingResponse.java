package com.twinkles.asaptrading.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AsapTradingResponse {
    private boolean successful;
    private String status;
    private String message;
    private int result;
    private Object data;
}
