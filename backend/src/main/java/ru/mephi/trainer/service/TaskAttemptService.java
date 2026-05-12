package ru.mephi.trainer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.*;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.exception.*;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.repository.TaskAttemptRepository;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.repository.TaskTrainerRepository;
import ru.mephi.trainer.rest.dto.response.MessageResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskAttemptService {
    private final TaskAttemptRepository taskAttemptRepository;
    private final TaskTrainerRepository taskTrainerRepository;
    private final ProfileRepository profileRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public MessageResponse insertTaskAttempt(UUID userId, UUID trainerId, UUID taskId, String answer) {
        try {
            TaskTrainerEntity taskTrainer = validateAndGetTaskTrainer(trainerId, taskId);
            UserEntity user = getUserOrThrow(userId);
            TaskEntity task = getTaskOrThrow(taskId);
            JsonNode config = objectMapper.readTree(task.getConfig());

            int count = getAndCheckAttemptsLimit(taskTrainer.getId(), userId, config);
            checkLastAttemptStatus(userId, taskTrainer.getId());

            boolean isCorrect;
            int points = 0;
            AttemptStatus status;
            int mistakeCost = config.get("mistakeCost").asInt();

            switch (task.getTaskType()) {
                case OPEN_ANSWER:
                    status = AttemptStatus.REVIEW;
                    saveAttempt(taskTrainer, user, answer, points, status);
                    return MessageResponse.builder()
                            .message("Ответ отправлен на проверку эксперту")
                            .timestamp(Instant.now().toString())
                            .build();

                case SINGLE_CHOICE:
                    isCorrect = checkSingleChoice(answer, config);
                    break;

                case MULTIPLE_CHOICE:
                    isCorrect = checkMultipleChoice(answer, config);
                    break;
                case ERROR_FINDING:
                    isCorrect = checkFindingError(answer, config);
                    break;

                default:
                    throw new EntityNotFoundException("Неподдерживаемый тип задачи: " + task.getTaskType());
            }
            points = isCorrect ? config.get("points").asInt() - mistakeCost * count : 0;
            status = isCorrect ? AttemptStatus.COMPLETED : AttemptStatus.FAILED;
            saveAttempt(taskTrainer, user, answer, points, status);

            String message = isCorrect
                    ? "Правильно! Вы получили " + points + " баллов"
                    : "Введен неверный ответ";

            return MessageResponse.builder()
                    .message(message)
                    .timestamp(Instant.now().toString())
                    .build();
        }
        catch (JsonProcessingException e) {
            log.error("Ошибка парсинга JSON: {}", e.getMessage());
            throw new RuntimeException("Неверный формат ответа", e);
        }
    }

    private TaskTrainerEntity validateAndGetTaskTrainer(UUID trainerId, UUID taskId) {
        return taskTrainerRepository
                .findByTrainerIdAndTaskId(trainerId, taskId)
                .orElseThrow(() -> {
                    log.warn("Get task in trainer failed - id not found: trainerId={}, taskId={}", trainerId, taskId);
                    return new EntityNotFoundException("Задача для отправки ответа не найдена");
                });
    }

    private UserEntity getUserOrThrow(UUID userId) {
        UserEntity user = profileRepository.getUserData(userId);
        if (user == null) {
            log.warn("User not found: userId={}", userId);
            throw new CurrentUserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    private TaskEntity getTaskOrThrow(UUID taskId) {
        return taskRepository.findByIdOptional(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found: taskId={}", taskId);
                    return new EntityNotFoundException("Задача не найдена");
                });
    }

    private int getAndCheckAttemptsLimit(UUID taskTrainerId, UUID userId, JsonNode config) {
        int attemptsCount = taskAttemptRepository.getAttemptsCount(taskTrainerId, userId);
        int maxAttempts = config.get("maxAttempts").asInt();

        if (attemptsCount >= maxAttempts) {
            throw new UserUseMaxAttemptsLimitException("Использованы все попытки (максимум: " + maxAttempts + ")");
        }
        return attemptsCount;
    }

    private void checkLastAttemptStatus(UUID userId, UUID taskTrainerId) {
        Optional<TaskAttemptEntity> lastAttempt = taskAttemptRepository
                .findLastAttempt(userId, taskTrainerId);

        if (lastAttempt.isEmpty()) {
            return;
        }

        TaskAttemptEntity attempt = lastAttempt.get();
        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            log.warn("Last task attempt is COMPLETED");
            throw new LastTaskAttemptStatusNotFailedException("Задача уже решена правильно");
        }
        if (attempt.getStatus() == AttemptStatus.REVIEW) {
            log.warn("Last task attempt on REVIEW");
            throw new LastTaskAttemptStatusNotFailedException("Предыдущий ответ уже на проверке");
        }
    }

    private void saveAttempt(TaskTrainerEntity taskTrainer, UserEntity user,
                             String answer, int points, AttemptStatus status) {
        TaskAttemptEntity attempt = TaskAttemptEntity.builder()
                .task(taskTrainer)
                .user(user)
                .userAnswer(answer)
                .points(points)
                .status(status)
                .build();
        taskAttemptRepository.persist(attempt);
    }

    private boolean checkSingleChoice(String answer, JsonNode config) throws JsonProcessingException {
        JsonNode answerNode = objectMapper.readTree(answer);
        if (!answerNode.isNumber()) {
            return false;
        }
        int userChoice = answerNode.asInt();
        int correctChoice = config.get("expectedOrdinal").asInt();
        return userChoice == correctChoice;
    }

    private boolean checkMultipleChoice(String answer, JsonNode config) throws JsonProcessingException {
        JsonNode answerNode = objectMapper.readTree(answer);
        if (!answerNode.isArray()) {
            return false;
        }
        JsonNode correctAnswers = config.get("expectedOrdinals");

        if (answerNode.size() != correctAnswers.size()) {
            return false;
        }

        for (JsonNode node : answerNode) {
            boolean found = false;
            for (JsonNode correct : correctAnswers) {
                if (node.asInt() == correct.asInt()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    private boolean checkFindingError(String answer, JsonNode config) throws JsonProcessingException {
        JsonNode answerNode = objectMapper.readTree(answer);
        String userChoice = answerNode.get("answer").asText();
        String correctChoice = config.get("answer").asText();
        return userChoice.equals(correctChoice);
    }
}