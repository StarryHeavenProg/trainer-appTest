package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.rest.dto.response.profile.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.profile.TrainerProgressPercentResponse;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileResponse getProfile(UUID userId) {
        log.info("User info profile requested for: {}", userId);

        UserEntity user = profileRepository.getUserData(userId);
        List<TrainerProgressPercentResponse> progress = profileRepository.getUserTrainersProgress(userId);
        Integer score = profileRepository.getUserTotalScore(userId);

        var response = new ProfileResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt().toString());
        response.setTotalScore(score);
        response.setTrainerProgressPercent(progress);

        return response;
    }
}