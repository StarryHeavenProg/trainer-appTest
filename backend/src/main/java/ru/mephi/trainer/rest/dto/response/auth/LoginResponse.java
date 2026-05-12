package ru.mephi.trainer.rest.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ при успешном входе")
public class LoginResponse {

    @Schema(description = "JWT токен", examples = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCJ9...")
    private String token;

    @Schema(description = "Email пользователя", examples = "user@example.com")
    private String email;

    @Schema(description = "Роль пользователя", examples = "student")
    private String role;
}
