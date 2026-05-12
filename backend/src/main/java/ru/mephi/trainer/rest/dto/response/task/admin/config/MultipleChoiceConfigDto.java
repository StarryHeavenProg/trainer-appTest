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
@Schema(title = "MultipleChoiceConfigDto", description = "Конфигурация задания с множественным выбором")
public class MultipleChoiceConfigDto implements TaskConfigDto {

    @Schema(description = "Текст вопроса",
            examples = "Какие утверждения верны для оператора WHERE в SQL?")
    private String question;

    @Schema(description = "Список вариантов ответов")
    private List<AnswerChoiceDto> answerChoices;

    @Schema(description = "Список порядковых номеров правильных ответов (начиная с 1)",
            examples = "[2, 4]")
    private List<Integer> expectedOrdinals;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "15",
            minimum = "1",
            maximum = "100")
    private Integer points;

    @Schema(description = "Штраф за каждый неправильно выбранный вариант ответа (в баллах)",
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
