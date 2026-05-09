package ru.mephi.trainer.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию пользователя")
public class RegistrationRequest {

    @Schema(
            description = "Email пользователя",
            examples = "user@example.com",
            required = true
    )
    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @Schema(
            description = "Пароль (минимум 6 символов)",
            examples = "password123",
            required = true,
            minLength = 6
    )
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @Schema(
            description = "Имя пользователя",
            examples = "Иван",
            required = true
    )
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @Schema(
            description = "Фамилия пользователя",
            examples = "Иванов",
            required = true
    )
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;
}
