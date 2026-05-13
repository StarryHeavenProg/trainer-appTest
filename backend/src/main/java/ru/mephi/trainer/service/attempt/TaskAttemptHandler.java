package ru.mephi.trainer.service.attempt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.TaskAttemptEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.attempt.SubmissionCheckResult;
import ru.mephi.trainer.models.attempt.answer.UserAnswer;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.repository.TaskAttemptRepository;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskAttemptHandler {
    private final TaskAttemptRepository taskAttemptRepository;
    private final AnswerCheckerRegistry checkerRegistry;
    private final ObjectMapper objectMapper;

    public SubmissionCheckResult handleReviewAttempt(TaskTrainerEntity taskTrainer,
                                                     UserEntity user,
                                                     UserAnswer answer) {
        saveAttempt(taskTrainer, user, answer, 0, AttemptStatus.REVIEW);
        return SubmissionCheckResult.builder()
                .attemptStatus(AttemptStatus.REVIEW)
                .message("Ответ отправлен на проверку эксперту")
                .build();
    }

    public <C extends TaskConfig, A extends UserAnswer> SubmissionCheckResult handleAutoCheckAttempt(
            TaskTrainerEntity taskTrainer,
            UserEntity user,
            C config,
            A parsedAnswer,
            TaskType taskType) {

        int attemptsCount = taskAttemptRepository.getAttemptsCount(taskTrainer.getId(), user.getId());
        AnswerChecker<C, A> checker = checkerRegistry.getChecker(taskType);
        boolean isCorrect = checker.check(parsedAnswer, config);

        if (!isCorrect) {
            saveAttempt(taskTrainer, user, parsedAnswer, 0, AttemptStatus.FAILED);
            return SubmissionCheckResult.builder()
                    .attemptStatus(AttemptStatus.FAILED)
                    .message("Введен неверный ответ")
                    .build();
        }

        int points = config.getPoints() - config.getMistakeCost() * attemptsCount;
        points = Math.max(0, points);
        saveAttempt(taskTrainer, user, parsedAnswer, points, AttemptStatus.COMPLETED);

        return SubmissionCheckResult.builder()
                .attemptStatus(AttemptStatus.COMPLETED)
                .message(String.format("Правильно! Вы получили %d баллов", points))
                .build();
    }

    private void saveAttempt(TaskTrainerEntity taskTrainer, UserEntity user,
                             UserAnswer answer, int points, AttemptStatus status) {
        TaskAttemptEntity attempt = TaskAttemptEntity.builder()
                .task(taskTrainer)
                .user(user)
                .userAnswer(serialize(answer))
                .points(points)
                .status(status)
                .build();
        taskAttemptRepository.persist(attempt);
    }

    private String serialize(UserAnswer userAnswer) {
        try {
            return objectMapper.writeValueAsString(userAnswer);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Ошибка при сериализации UserAnswer: " + e.getMessage());
        }
    }
}
