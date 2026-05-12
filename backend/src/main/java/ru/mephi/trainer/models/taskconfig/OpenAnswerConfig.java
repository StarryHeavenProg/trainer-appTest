package ru.mephi.trainer.models.taskconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAnswerConfig implements TaskConfig {
    private String question;
    private String context;
    private String expectedAnswer;
    private Integer points;
    private Integer mistakeCost;
    private Integer maxAttempts;

    @Override
    public TaskType getTaskType() {
        return TaskType.OPEN_ANSWER;
    }
}
