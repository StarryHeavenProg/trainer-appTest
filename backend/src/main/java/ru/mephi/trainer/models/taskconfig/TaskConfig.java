package ru.mephi.trainer.models.taskconfig;

import ru.mephi.trainer.entity.enums.TaskType;

public interface TaskConfig {
    TaskType getTaskType();
    String getQuestion();
    Integer getPoints();
    Integer getMaxAttempts();
    Integer getMistakeCost();
}
