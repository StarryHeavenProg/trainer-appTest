package ru.mephi.trainer.rest.dto.request.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "OpenAnswerConfigRequestDto", description = "Конфигурация задания с открытым ответом")
public class OpenAnswerConfigRequestDto implements TaskConfigRequestDto {

    @Schema(description = "Текст вопроса",
            examples = "Как вы поступите и какие аргументы приведёте заказчику?",
            required = true)
    @NotBlank(message = "Текст вопроса не может быть пустым")
    @Size(min = 5, max = 1000, message = "Текст вопроса должен быть от 5 до 1000 символов")
    private String question;

    @Schema(description = "Контекст задания (описание ситуации)",
            examples = "Заказчик просит добавить в релиз через 3 дня новую фичу «Экспорт в Excel»...",
            required = true)
    @NotBlank(message = "Контекст задания не может быть пустым")
    @Size(min = 10, max = 5000, message = "Контекст должен быть от 10 до 5000 символов")
    private String context;

    @Schema(description = "Ожидаемый ответ (для подсказки эксперту)",
            examples = "Предложить перенести фичу в следующий релиз")
    private String expectedAnswer;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "20",
            required = true)
    @NotNull(message = "Количество баллов не может быть пустым")
    @Positive(message = "Количество баллов должно быть положительным числом")
    @Max(value = 100, message = "Количество баллов не может превышать 100")
    private Integer points;

    @Schema(description = "Штраф за неправильный ответ (в баллах)",
            examples = "5",
            required = true)
    @NotNull(message = "Штраф за ошибку не может быть пустым")
    @Min(value = 0, message = "Штраф не может быть отрицательным")
    @Max(value = 100, message = "Штраф не может превышать 100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток",
            examples = "2",
            required = true)
    @NotNull(message = "Количество попыток не может быть пустым")
    @Min(value = 1, message = "Количество попыток должно быть не меньше 1")
    @Max(value = 10, message = "Количество попыток не может превышать 10")
    private Integer maxAttempts;

    @Override
    public TaskType getTaskType() {
        return TaskType.OPEN_ANSWER;
    }
}
