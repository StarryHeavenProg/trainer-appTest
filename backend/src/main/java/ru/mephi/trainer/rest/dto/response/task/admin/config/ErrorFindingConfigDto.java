package ru.mephi.trainer.rest.dto.response.task.admin.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ErrorFindingConfigDto", description = "Конфигурация задания на поиск ошибок")
public class ErrorFindingConfigDto implements TaskConfigDto {

    @Schema(description = "Текст вопроса",
            examples = "Найдите недостатки или риски валидации.")
    private String question;

    @Schema(description = "Ответ")
    private String answer;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "20",
            minimum = "1",
            maximum = "100")
    private Integer points;

    @Schema(description = "Штраф за каждый неправильно выбранный вариант (в баллах)",
            examples = "3",
            minimum = "0",
            maximum = "100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток выполнения задания",
            examples = "2",
            minimum = "1",
            maximum = "10")
    private Integer maxAttempts;
}
