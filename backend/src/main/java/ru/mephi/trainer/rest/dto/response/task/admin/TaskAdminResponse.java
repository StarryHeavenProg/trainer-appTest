package ru.mephi.trainer.rest.dto.response.task.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.rest.dto.response.task.admin.config.ErrorFindingConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.MultipleChoiceConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.OpenAnswerConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.SingleChoiceConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.TaskConfigDto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о задании для администратора/эксперта (с правильными ответами)")
public class TaskAdminResponse {

    @Schema(description = "ID задания", examples = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Тип задания", examples = "SINGLE_CHOICE")
    private TaskType taskType;

    @Schema(description = "Конфигурация задания (зависит от типа)",
            oneOf = {
                    SingleChoiceConfigDto.class,
                    MultipleChoiceConfigDto.class,
                    ErrorFindingConfigDto.class,
                    OpenAnswerConfigDto.class,
            })
    private TaskConfigDto config;

    @Schema(description = "ID создателя задания", examples = "00000000-0000-0000-0000-000000000001")
    private UUID createdBy;

    @Schema(description = "Дата создания", examples = "2024-01-15T10:30:00+03:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Instant createdAt;

    @Schema(description = "Список ID тренажёров, к которым привязано задание",
            examples = "[\"550e8400-e29b-41d4-a716-446655440001\"]")
    private Set<UUID> trainerIds;
}
