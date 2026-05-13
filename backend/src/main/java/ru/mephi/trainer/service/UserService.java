package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.exception.EntityNotFoundException;
import ru.mephi.trainer.models.TrainerPercentProgress;
import ru.mephi.trainer.models.UserProfile;
import ru.mephi.trainer.repository.ProfileRepository;
import ru.mephi.trainer.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserEntity getUser(UUID userId) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public UserProfile getProfile(UUID userId) {
        log.info("User info profile requested for: {}", userId);

        UserEntity user = getUser(userId);
        List<TrainerPercentProgress> progress = profileRepository.getUserTrainersProgress(userId);
        Integer score = profileRepository.getUserTotalScore(userId);

        return UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .totalScore(score)
                .trainerProgressPercent(progress)
                .build();
    }
}
