package ru.mephi.trainer.rest.dto.request.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.mephi.trainer.entity.enums.TaskType;

public interface TaskConfigRequestDto {
    @JsonIgnore
    TaskType getTaskType();
    String getQuestion();
    Integer getPoints();
    Integer getMistakeCost();
    Integer getMaxAttempts();
}
