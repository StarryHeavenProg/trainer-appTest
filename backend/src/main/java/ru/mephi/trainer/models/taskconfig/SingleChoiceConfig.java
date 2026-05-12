package ru.mephi.trainer.models.taskconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleChoiceConfig implements TaskConfig {
    private String question;
    private List<AnswerChoice> answerChoices;
    private Integer expectedOrdinal;
    private Integer points;
    private Integer mistakeCost;
    private Integer maxAttempts;

    @Override
    public TaskType getTaskType() {
        return TaskType.SINGLE_CHOICE;
    }
}
