package com.twinkles.asaptrading.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
