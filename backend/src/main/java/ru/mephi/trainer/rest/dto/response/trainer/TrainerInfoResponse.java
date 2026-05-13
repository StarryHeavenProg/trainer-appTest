package ru.mephi.trainer.rest.dto.response.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;

import java.time.Instant;
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
    private Instant createdAt;
    private UUID createdBy;
    private List<TaskResponse> tasks;
}
