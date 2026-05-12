package ru.mephi.trainer.rest.dto.response.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.rest.dto.response.task.user.TaskInfoResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerInfoResponse {
    private UUID id;
    private String name;
    private Integer totalTasks;
    private OffsetDateTime createdAt;
    private UUID createdBy;
    private List<TaskInfoResponse> tasks;
}
