package com.twinkles.asaptrading.entity;

import com.twinkles.asaptrading.enums.AccountDetailsType;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Validated
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    private String accountNumber;
    private String accountName;
    @Enumerated(EnumType.STRING)
    private AccountDetailsType accountDetailsType;
}
