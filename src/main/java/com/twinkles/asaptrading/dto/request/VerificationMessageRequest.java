package com.twinkles.asaptrading.dto.request;

import lombok.*;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VerificationMessageRequest extends EmailRequest {
    @Email
    private String sender;
    @Email
    private String receiver;
    private String subject;
    private String body;
    private String usersFullName;
    private String verificationToken;
    private String domainUrl;




}
