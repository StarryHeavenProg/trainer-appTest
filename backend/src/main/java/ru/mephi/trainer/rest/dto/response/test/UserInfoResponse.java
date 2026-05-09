package ru.mephi.trainer.rest.dto.response.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о текущем пользователе из JWT")
public class UserInfoResponse {

    @Schema(description = "Email пользователя", examples = "user@example.com")
    private String email;

    @Schema(description = "Роль пользователя", examples = "student")
    private String role;

    @Schema(description = "ID пользователя", examples = "dcc1e942-db50-4c04-b41b-b80eaa0421ac")
    private UUID userId;
}
