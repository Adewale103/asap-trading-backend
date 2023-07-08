package com.twinkles.asaptrading.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericResponse {
    private boolean successful;
    private String message;
    private Object data;
}
