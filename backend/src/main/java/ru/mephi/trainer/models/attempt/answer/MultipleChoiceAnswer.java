package ru.mephi.trainer.models.attempt.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceAnswer implements UserAnswer {
    private Set<Integer> selectedOrdinals;

    @Override
    public TaskType getTaskType() {
        return TaskType.MULTIPLE_CHOICE;
    }
}
