package ru.mephi.trainer.rest.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

    private String token;
    private String message;
    @Builder.Default
    private final Instant timestamp = Instant.now();
}
