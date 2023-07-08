package com.twinkles.asaptrading.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserDetailsRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String bitcoinWalletAddress;
    private String ethereumWalletAddress;
    private String usdtWalletAddress;
    private String bnbSmartChainWalletAddress;
    private String bnbBeaconChainWalletAddress;
    private String bnbMemoWalletAddress;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountName;
}
