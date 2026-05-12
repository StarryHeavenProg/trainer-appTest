package ru.mephi.trainer.rest.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на вход в систему")
public class LoginRequest {

    @Schema(
            description = "Email пользователя",
            examples = "user@example.com",
            required = true
    )
    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @Schema(
            description = "Пароль",
            examples = "password123",
            required = true
    )
    @NotBlank(message = "Пароль обязателен")
    private String password;
}
