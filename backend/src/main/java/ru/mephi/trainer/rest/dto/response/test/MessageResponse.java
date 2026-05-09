package ru.mephi.trainer.rest.dto.response.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Простой текстовый ответ")
public class MessageResponse {

    @Schema(description = "Сообщение", examples = "Операция выполнена успешно")
    private String message;

    @Schema(description = "Временная метка", examples = "2024-01-15T10:30:00Z")
    private String timestamp;
}
