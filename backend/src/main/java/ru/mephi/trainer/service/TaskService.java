package ru.mephi.trainer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.TaskTrainerEntity;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.exception.InvalidCommandDataException;
import ru.mephi.trainer.models.command.SaveTaskCommand;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.repository.TrainerRepository;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;
import ru.mephi.trainer.service.taskconfig.TaskConfigValidator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TrainerRepository trainerRepository;
    private final TaskConfigValidator taskConfigValidator;
    private final ObjectMapper objectMapper;

    public TaskEntity getTask(UUID taskId) {
        return taskRepository.findByIdOptional(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена: " + taskId));
    }

    public TaskResponse getTaskWithAttempt(UUID userId, UUID trainerId, UUID taskId) {
        log.info("Get task with user attempt: userId={}, trainerId={}, taskId={}", userId, trainerId, taskId);
        return taskRepository.getTaskWithUserAttempt(userId, trainerId, taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found: taskId={}, trainerId={}", taskId, trainerId);
                    return new EntityNotFoundException(
                            "Задача не найдена в этом тренажёре. taskId: " + taskId + " trainerId: " + trainerId
                    );
                });
    }

    @Transactional
    public TaskEntity createTask(SaveTaskCommand command, UUID createdBy) {
        log.info("Creating task of type: {}", command.getConfig().getTaskType());

        validateConfig(command.getConfig());

        String configJson = serializeConfig(command.getConfig());

        TaskEntity task = TaskEntity.builder()
                .taskType(command.getConfig().getTaskType())
                .config(configJson)
                .createdBy(createdBy)
                .build();

        taskRepository.persist(task);
        assignToTrainers(task, command.getTrainerIds());

        return task;
    }

    @Transactional
    public TaskEntity updateTask(UUID taskId, SaveTaskCommand command) {
        log.info("Updating task: id={}, type={}", taskId, command.getConfig().getTaskType());

        validateConfig(command.getConfig());

        TaskEntity task = taskRepository.findByIdWithTrainersLinks(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));

        String configJson = serializeConfig(command.getConfig());
        task.setConfig(configJson);

        taskRepository.persist(task);
        reassignTaskToTrainers(task, command.getTrainerIds());

        return task;
    }

    private void assignToTrainers(TaskEntity task, Set<UUID> trainerIds) {
        if (trainerIds == null || trainerIds.isEmpty()) {
            return;
        }

        Map<UUID, TrainerEntity> trainerMap = trainerRepository.findByIds(trainerIds)
                .stream()
                .collect(Collectors.toMap(TrainerEntity::getId, Function.identity()));

        Set<UUID> notFound = new HashSet<>(trainerIds);
        notFound.removeAll(trainerMap.keySet());
        if (!notFound.isEmpty()) {
            throw new EntityNotFoundException("Тренажёры не найдены: " + notFound);
        }

        for (TrainerEntity trainer : trainerMap.values()) {
            TaskTrainerEntity link = TaskTrainerEntity.builder()
                    .task(task)
                    .trainer(trainer)
                    .build();
            task.getTrainerLinks().add(link);
        }
    }

    private void reassignTaskToTrainers(TaskEntity task, Set<UUID> newTrainerIds) {
        if (newTrainerIds == null) {
            return;
        }

        Set<UUID> currentIds = task.getTrainerLinks().stream()
                .map(link -> link.getTrainer().getId())
                .collect(Collectors.toSet());

        Set<UUID> trainersToRemove = new HashSet<>(currentIds);
        trainersToRemove.removeAll(newTrainerIds);

        Set<UUID> trainersToAdd = new HashSet<>(newTrainerIds);
        trainersToAdd.removeAll(currentIds);

        if (!trainersToRemove.isEmpty()) {
            task.getTrainerLinks().removeIf(link -> trainersToRemove.contains(link.getTrainer().getId()));
        }

        if (!trainersToAdd.isEmpty()) {
            Map<UUID, TrainerEntity> trainerMap = trainerRepository.findByIds(trainersToAdd)
                    .stream()
                    .collect(Collectors.toMap(TrainerEntity::getId, Function.identity()));

            Set<UUID> notFound = new HashSet<>(trainersToAdd);
            notFound.removeAll(trainerMap.keySet());
            if (!notFound.isEmpty()) {
                throw new EntityNotFoundException("Тренажёры не найдены: " + notFound);
            }

            for (TrainerEntity trainer : trainerMap.values()) {
                TaskTrainerEntity link = TaskTrainerEntity.builder()
                        .task(task)
                        .trainer(trainer)
                        .build();
                task.addTrainerLink(link);
            }
        }
    }

    private String serializeConfig(TaskConfig config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (Exception e) {
            log.error("Failed to serialize config", e);
            throw new IllegalArgumentException("Ошибка сериализации конфигурации задания");
        }
    }

    private void validateConfig(TaskConfig config) {
        Set<String> errors = taskConfigValidator.validateConfig(config);

        if (!errors.isEmpty()) {
            throw new InvalidCommandDataException(errors);
        }
    }
}
