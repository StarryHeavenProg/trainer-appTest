package ru.mephi.trainer.models.attempt.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorFindingAnswer implements UserAnswer {
    private String answer;

    @Override
    public TaskType getTaskType() {
        return TaskType.ERROR_FINDING;
    }
}
