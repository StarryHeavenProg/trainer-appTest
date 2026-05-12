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
@Schema(title = "OpenAnswerConfigDto", description = "Конфигурация задания с открытым ответом")
public class OpenAnswerConfigDto implements TaskConfigDto {

    @Schema(description = "Текст вопроса",
            examples = "Как вы поступите и какие аргументы приведёте заказчику?")
    private String question;

    @Schema(description = "Контекст задания (описание ситуации)",
            examples = "Заказчик просит добавить в релиз через 3 дня новую фичу «Экспорт в Excel»...")
    private String context;

    @Schema(description = "Ожидаемый ответ (для эксперта, не показывается пользователю)",
            examples = "Предложить перенести фичу в следующий релиз или найти компромисс")
    private String expectedAnswer;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "25",
            minimum = "1",
            maximum = "100")
    private Integer points;

    @Schema(description = "Штраф за неправильный ответ (в баллах)",
            examples = "5",
            minimum = "0",
            maximum = "100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток выполнения задания",
            examples = "2",
            minimum = "1",
            maximum = "10")
    private Integer maxAttempts;
}
