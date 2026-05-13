package ru.mephi.trainer.rest.dto.response.task.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private TaskType taskType;
    private String question;
    private String answerChoices;
    private String context;
    private String answer;
    private Integer points;
    private Integer mistakeCost;
    private Integer maxAttempts;
    private Integer userPoints;
    private Integer userAttempts;
    private AttemptStatus attemptStatus;
}