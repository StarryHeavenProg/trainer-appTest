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
public class SingleChoiceAnswer implements UserAnswer {
    private Integer selectedOrdinal;

    @Override
    public TaskType getTaskType() {
        return TaskType.SINGLE_CHOICE;
    }
}
