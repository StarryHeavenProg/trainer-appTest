package ru.mephi.trainer.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Простой текстовый ответ")
public class MessageResponse {

    @Schema(description = "Сообщение", examples = "Операция выполнена успешно")
    private String message;

    @Builder.Default
    @Schema(description = "Временная метка", examples = "2024-01-15T10:30:00Z")
    private Instant timestamp = Instant.now();

    public static MessageResponse withMessage(String message) {
        return new MessageResponse(message, Instant.now());
    }
}
