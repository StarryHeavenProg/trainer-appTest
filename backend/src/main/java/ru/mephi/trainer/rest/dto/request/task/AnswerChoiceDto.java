package ru.mephi.trainer.rest.dto.request.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Вариант ответа")
public class AnswerChoiceDto {

    @Schema(description = "Порядковый номер варианта (начиная с 1)",
            examples = "1",
            required = true)
    @NotNull(message = "Порядковый номер варианта не может быть пустым")
    @Min(value = 1, message = "Порядковый номер должен быть не меньше 1")
    private Integer ordinal;

    @Schema(description = "Текст варианта ответа",
            examples = "INNER JOIN возвращает только совпадающие записи",
            required = true)
    @NotBlank(message = "Текст варианта ответа не может быть пустым")
    private String choice;
}
