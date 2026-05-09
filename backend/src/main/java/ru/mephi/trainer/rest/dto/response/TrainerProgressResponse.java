package ru.mephi.trainer.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProgressResponse {
    private UUID trainerId;
    private String trainerName;
    private Integer earnedScore;      // заработал
    private Integer maxPossibleScore; // максимум можно
    private Integer tasksCompleted;   // решено задач
    private Integer totalTasks;       // всего задач в тренажёре
    private List<CompletedTaskTrainerPointResponse> tasksInTrainer;  // баллы за тренажер
}
