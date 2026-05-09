package ru.mephi.trainer.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
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
}
