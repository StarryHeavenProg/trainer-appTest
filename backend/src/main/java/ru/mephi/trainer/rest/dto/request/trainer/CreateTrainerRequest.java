package ru.mephi.trainer.rest.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание тренажера")
public class CreateTrainerRequest {

    @Schema(
            description = "Наименование тренажера",
            examples = {"API и интеграции", "Основы SQL"},
            required = true
    )
    @NotBlank
    @Size(min = 3)
    private String name;
}
