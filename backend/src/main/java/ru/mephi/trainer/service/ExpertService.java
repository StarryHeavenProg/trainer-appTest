package ru.mephi.trainer.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TaskAttemptEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.repository.TaskAttemptRepository;
import ru.mephi.trainer.rest.dto.response.task.expert.AnswerTaskResponse;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.rest.dto.response.task.expert.ReviewTaskResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ExpertService {
    private final TaskAttemptRepository taskAttemptRepository;

    public List<ReviewTaskResponse> getReviewTask() {
        log.info("Get all tasks for review");
        return taskAttemptRepository.getReviewTasksWithDetails();
    }

    public AnswerTaskResponse getTaskForCheck(UUID taskAttemptId) {
        log.info("Get task for review info: id={}", taskAttemptId);
        return taskAttemptRepository.getAnswerTask(taskAttemptId)
                .orElseThrow(() -> {
                    log.warn("Get task for review info failed - id not found: id={}", taskAttemptId);
                    return new EntityNotFoundException("Ответ для проверки с id " + taskAttemptId + " не найден");
                });
    }

    @Transactional
    public MessageResponse setPointsForTask(UUID taskAttemptId, Boolean isCorrect) {
        log.info("Setting points for attempt: {} with points: {}", taskAttemptId, isCorrect);
        TaskAttemptEntity taskAttempt = taskAttemptRepository.findByIdOptional(taskAttemptId)
                .orElseThrow(() -> {
                    log.warn("Get task for review info failed - id not found: id={}", taskAttemptId);
                    return new EntityNotFoundException("Ответ для проверки с id " + taskAttemptId + " не найден");
                });
        if (taskAttempt.getStatus() != AttemptStatus.REVIEW) {
            log.warn("Get task for review info failed - task not in review: id={}", taskAttemptId);
            throw new BadRequestException("Работа не на проверке");
        }

        int points = 0;
        if (!isCorrect) {
            taskAttempt.setPoints(points);
            taskAttempt.setStatus(AttemptStatus.FAILED);
        }
        else {
            int attemptsCount = taskAttemptRepository.getAttemptsCount(taskAttempt.getTask().getId(),
                    taskAttempt.getUser().getId());
            int maxPointsForAttempt = taskAttemptRepository.getMaxPointsForAttempt(taskAttemptId);
            int mistakeCost = taskAttemptRepository.getMistakeCost(taskAttemptId);

            points = maxPointsForAttempt;
            if (attemptsCount > 1) {
                points -= mistakeCost * (attemptsCount - 1);
            }
            taskAttempt.setPoints(points);
            taskAttempt.setStatus(AttemptStatus.COMPLETED);
        }

        taskAttemptRepository.persist(taskAttempt);
        return MessageResponse.builder()
                .message("Работа оценена. Поставлено баллов: " + points)
                .timestamp(Instant.now().toString())
                .build();
    }
}
