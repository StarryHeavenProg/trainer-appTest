package ru.mephi.trainer.service;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.exception.TrainerNotFoundException;
import ru.mephi.trainer.repository.TrainerRepository;
import ru.mephi.trainer.rest.dto.response.TrainerInfoResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public List<TrainerEntity> getAllTrainers() {
        log.info("Get all trainers");
        return trainerRepository.listAll();
    }

    public TrainerInfoResponse getTrainerInfo(UUID id) {
        log.info("Get trainer info: id={}", id);

        TrainerEntity entity = trainerRepository.findByIdOptional(id)
                .orElseThrow(() -> {
                    log.warn("Get trainer info failed - id not found: id={}", id);
                    return new TrainerNotFoundException("Тренажёр с id " + id + " не найден");
                });

        Integer totalTasks = trainerRepository.getTotalTasks(id);

        return TrainerInfoResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalTasks(totalTasks)
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }
}
