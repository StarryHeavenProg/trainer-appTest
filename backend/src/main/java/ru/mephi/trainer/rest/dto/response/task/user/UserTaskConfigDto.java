package ru.mephi.trainer.rest.dto.response.task.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.rest.dto.request.task.AnswerChoiceDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Конфигурация задания для пользователя")
public class UserTaskConfigDto {
    private TaskType type;
    private String question;
    private List<AnswerChoiceDto> answerChoices;
    private String context;
    private Integer points;
    private Integer maxAttempts;
}