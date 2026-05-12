package ru.mephi.trainer.rest.dto.response.task.admin.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.rest.dto.request.task.AnswerChoiceDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "SingleChoiceConfigDto", description = "Конфигурация задания с одиночным выбором")
public class SingleChoiceConfigDto implements TaskConfigDto {

    @Schema(description = "Текст вопроса",
            examples = "Какой тип требования описывает, что система должна делать?")
    private String question;

    @Schema(description = "Список вариантов ответов")
    private List<AnswerChoiceDto> answerChoices;

    @Schema(description = "Порядковый номер правильного ответа (начиная с 1)",
            examples = "3")
    private Integer expectedOrdinal;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "10",
            minimum = "1",
            maximum = "100")
    private Integer points;

    @Schema(description = "Штраф за неправильный ответ (в баллах)",
            examples = "2",
            minimum = "0",
            maximum = "100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток выполнения задания",
            examples = "3",
            minimum = "1",
            maximum = "10")
    private Integer maxAttempts;
}
