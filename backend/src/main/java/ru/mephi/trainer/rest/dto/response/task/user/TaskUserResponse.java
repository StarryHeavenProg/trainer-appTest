package ru.mephi.trainer.rest.dto.response.task.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Ответ с информацией о задании для пользователя (без правильных ответов)")
public class TaskUserResponse {

    @Schema(description = "ID задания")
    private UUID id;

    @Schema(description = "Тип задания")
    private TaskType taskType;

    @Schema(description = "Конфигурация задания для пользователя")
    private UserTaskConfigDto config;

    @Schema(description = "Дата создания")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @Schema(description = "Список ID тренажёров")
    private Set<UUID> trainerIds;
}
