package ru.mephi.trainer.rest.dto.request.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание/обновление задания")
public abstract class SaveTaskRequest {

    @Schema(description = "Список ID тренажёров, к которым привязать задание",
            examples = "[\"29f18957-fffd-4d83-b738-9f6e0496bf3d\", \"8088c009-41c7-4a79-99e0-7d91705cf967\"]"
    )
    private Set<UUID> trainerIds;

    @NotNull(message = "Тип задания обязателен")
    @Schema(description = "Тип задания", required = true)
    private TaskType taskType;

    @Valid
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "taskType",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SingleChoiceConfigRequestDto.class, name = "SINGLE_CHOICE"),
            @JsonSubTypes.Type(value = MultipleChoiceConfigRequestDto.class, name = "MULTIPLE_CHOICE"),
            @JsonSubTypes.Type(value = ErrorFindingConfigRequestDto.class, name = "ERROR_FINDING"),
            @JsonSubTypes.Type(value = OpenAnswerConfigRequestDto.class, name = "OPEN_ANSWER")
    })
    @Schema(oneOf = {
            SingleChoiceConfigRequestDto.class,
            MultipleChoiceConfigRequestDto.class,
            ErrorFindingConfigRequestDto.class,
            OpenAnswerConfigRequestDto.class
    })
    @NotNull(message = "Конфигурация задания обязательна")
    private TaskConfigRequestDto config;
}
