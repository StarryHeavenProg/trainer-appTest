package ru.mephi.trainer.rest.dto.request.task;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "MultipleChoiceConfigRequestDto", description = "Конфигурация задания с множественным выбором")
public class MultipleChoiceConfigRequestDto implements TaskConfigRequestDto {

    @Schema(description = "Текст вопроса",
            examples = "Какие утверждения верны для оператора WHERE в SQL?",
            required = true)
    @NotBlank(message = "Текст вопроса не может быть пустым")
    @Size(min = 5, max = 1000, message = "Текст вопроса должен быть от 5 до 1000 символов")
    private String question;

    @Schema(description = "Варианты ответов",
            required = true)
    @NotEmpty(message = "Должен быть хотя бы один вариант ответа")
    @Size(min = 2, max = 10, message = "Количество вариантов ответа должно быть от 2 до 10")
    @Valid
    private List<AnswerChoiceDto> answerChoices;

    @Schema(description = "Список порядковых номеров правильных ответов (начиная с 1)",
            examples = "[2, 4]",
            required = true)
    @NotEmpty(message = "Необходимо указать хотя бы один правильный ответ")
    @Size(max = 10, message = "Количество правильных ответов не может превышать 10")
    private List<@Min(1) Integer> expectedOrdinals;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "10",
            required = true)
    @NotNull(message = "Количество баллов не может быть пустым")
    @Positive(message = "Количество баллов должно быть положительным числом")
    @Max(value = 100, message = "Количество баллов не может превышать 100")
    private Integer points;

    @Schema(description = "Штраф за неправильный ответ (в баллах)",
            examples = "2",
            required = true)
    @NotNull(message = "Штраф за ошибку не может быть пустым")
    @Min(value = 0, message = "Штраф не может быть отрицательным")
    @Max(value = 100, message = "Штраф не может превышать 100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток",
            examples = "3",
            defaultValue = "1",
            required = true)
    @NotNull(message = "Количество попыток не может быть пустым")
    @Min(value = 1, message = "Количество попыток должно быть не меньше 1")
    @Max(value = 10, message = "Количество попыток не может превышать 10")
    private Integer maxAttempts;

    @Override
    public TaskType getTaskType() {
        return TaskType.MULTIPLE_CHOICE;
    }
}
