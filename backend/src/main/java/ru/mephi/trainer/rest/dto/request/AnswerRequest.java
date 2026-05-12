package ru.mephi.trainer.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    @NotNull(message = "Ответ пользователя обязателен")
    private String userAnswer;
}
