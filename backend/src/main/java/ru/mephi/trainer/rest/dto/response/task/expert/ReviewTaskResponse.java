package ru.mephi.trainer.rest.dto.response.task.expert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewTaskResponse {
    private UUID attemptId;
    private String taskName;
    private String trainerName;
    private String studentName;
    private String studentEmail;
    private Instant createdAt;
}