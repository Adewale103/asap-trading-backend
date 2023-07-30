package com.twinkles.asaptrading.exception;

import lombok.Getter;

@Getter
public class AsapTradingException extends RuntimeException {
    private int statusCode;
    public AsapTradingException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
