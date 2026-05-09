package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.repository.TrainerProgressRepository;
import ru.mephi.trainer.rest.dto.response.CompletedTaskTrainerPointResponse;
import ru.mephi.trainer.rest.dto.response.TrainerProgressResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerProgressService {
    private final TrainerProgressRepository trainerProgressRepository;

    public TrainerProgressResponse getTrainerProgress(UUID userId, UUID trainerId) {
        log.info("User info requested for trainer progress: userId={}, trainerId={}", userId, trainerId);
        TrainerProgressResponse response = trainerProgressRepository.getTrainerProgress(userId, trainerId)
                .orElseThrow(() -> {
                    log.warn("Get trainer progress failed - id not found: id={}", trainerId);
                    return new TrainerNotFoundException("Тренажёр с id " + trainerId + " не найден");
                });
        List<CompletedTaskTrainerPointResponse> tasks = trainerProgressRepository.getCompletedTaskTrainer(userId, trainerId);
        response.setTasksInTrainer(tasks);
        return response;
    }
}