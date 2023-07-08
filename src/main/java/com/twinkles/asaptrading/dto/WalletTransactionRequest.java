package com.twinkles.asaptrading.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
public class WalletTransactionRequest {
    @NonNull
    private String amount;
    @NonNull
    private String email;
    private String reference;
    private final String callbackUrl = "localhost:8080/api/v1/parkingLot/wallet/";
    private Integer invoice_limit;
    private List<Channels> channels = new ArrayList<>();
    private String subAccount;
    private Integer transaction_charge;
    private final PaystackBearer paystackBearer = PaystackBearer.ACCOUNT;
    @NonNull
    private Long walletId;
}
