package ru.mephi.trainer.rest.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ProfileApi;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.TrainerProgressResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.ProfileService;
import ru.mephi.trainer.service.TrainerProgressService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class ProfileController implements ProfileApi {
    private final JsonWebToken jwt;
    private final CurrentUserService currentUserService;
    private final TrainerProgressService trainerProgressService;
    private final ProfileService profileService;

    @Override
    @Authenticated
    public RestResponse<ProfileResponse> getProfile() {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Profile endpoint accessed by: {}", userId);
        ProfileResponse response = profileService.getProfile(userId);
        log.info("Profile endpoint in successfully: {}", userId);

        return RestResponse.ok(response);
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