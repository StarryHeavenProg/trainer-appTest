package ru.mephi.trainer.rest.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.mapper.ProfileMapper;
import ru.mephi.trainer.models.UserProfile;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.profile.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.profile.TrainerProgressResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.UserService;
import ru.mephi.trainer.service.TrainerProgressService;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {

    private final CurrentUserService currentUserService;
    private final TrainerProgressService trainerProgressService;
    private final UserService userService;
    private final ProfileMapper profileMapper;

    @Override
    @Authenticated
    public RestResponse<ProfileResponse> getProfile() {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Profile endpoint accessed by: {}", userId);

        UserProfile profile = userService.getProfile(userId);

        return RestResponse.ok(profileMapper.toProfileResponse(profile));
    }

    @Override
    @Authenticated
    public RestResponse<TrainerProgressResponse> getTrainerProgress(UUID trainerId) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Trainer progress endpoint accessed by: {}", userId);
        TrainerProgressResponse response = trainerProgressService.getTrainerProgress(userId, trainerId);
        log.info("Trainer progress in successfully:  {}", userId);

        return RestResponse.ok(response);
    }
}