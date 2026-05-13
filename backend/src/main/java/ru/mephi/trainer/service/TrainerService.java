package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.exception.InvalidCommandDataException;
import ru.mephi.trainer.models.command.SaveTrainerCommand;
import ru.mephi.trainer.repository.TaskRepository;
import ru.mephi.trainer.repository.TrainerRepository;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerInfoResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TaskRepository taskRepository;

    public List<TrainerEntity> getAllTrainers() {
        log.info("Get all trainers");
        return trainerRepository.listAll();
    }

    public TrainerInfoResponse getTrainerInfo(UUID userId, UUID id) {
        log.info("Get trainer info: id={}", id);

        TrainerEntity entity = trainerRepository.findByIdOptional(id)
                .orElseThrow(() -> {
                    log.warn("Get trainer info failed - id not found: id={}", id);
                    return new EntityNotFoundException("Тренажёр с id " + id + " не найден");
                });

        Integer totalTasks = trainerRepository.getTotalTasks(id);

        List<TaskResponse> taskInfoResponseList = taskRepository.getTasksWithUserAttempt(userId, id);

        return TrainerInfoResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalTasks(totalTasks)
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .tasks(taskInfoResponseList)
                .build();
    }

    @Transactional
    public TrainerEntity createTrainer(SaveTrainerCommand saveTrainerCommand, UUID userId) {
        log.info("User {} creating trainer {}", userId, saveTrainerCommand.name());
        validateCreateTrainerCommand(saveTrainerCommand);

        TrainerEntity trainer = buildTrainerEntity(saveTrainerCommand, userId);
        trainerRepository.persist(trainer);

        return trainer;
    }

    private void validateCreateTrainerCommand(SaveTrainerCommand command) {
        if (command == null) {
            throw new InvalidCommandDataException("Command can not be null");
        }
        if (command.name() == null || command.name().trim().length() < 3) {
            throw new InvalidCommandDataException("Trainer name must contain at least 3 characters");
        }
    }

    private TrainerEntity buildTrainerEntity(SaveTrainerCommand saveTrainerCommand, UUID userId) {
        return TrainerEntity.builder()
                .name(saveTrainerCommand.name())
                .createdBy(userId)
                .build();
    }
}
