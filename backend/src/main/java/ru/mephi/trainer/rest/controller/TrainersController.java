package ru.mephi.trainer.rest.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.models.attempt.SubmissionCheckResult;
import ru.mephi.trainer.models.command.SaveTrainerCommand;
import ru.mephi.trainer.rest.api.TrainersAPI;
import ru.mephi.trainer.rest.dto.request.AnswerRequest;
import ru.mephi.trainer.rest.dto.request.trainer.CreateTrainerRequest;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.TaskAttemptService;
import ru.mephi.trainer.service.TaskService;
import ru.mephi.trainer.service.TrainerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TrainersController implements TrainersAPI {

    private final TrainerService trainerService;
    private final TaskAttemptService taskAttemptService;
    private final CurrentUserService currentUserService;
    private final TaskService taskService;

    @Override
    @Authenticated
    public RestResponse<List<TrainerResponse>> getTrainers() {
        log.info("Get all trainer");
        List<TrainerResponse> response = trainerService.getAllTrainers().stream()
                .map(t -> TrainerResponse.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .createdAt(t.getCreatedAt())
                        .createdBy(t.getCreatedBy())
                        .build())
                .toList();
        return RestResponse.ok(response);
    }

    @Override
    @Authenticated
    public RestResponse<TrainerInfoResponse> getTrainerInfo(UUID trainerId) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Get trainer: user={}, id={}", userId, trainerId);
        TrainerInfoResponse trainerInfoResponse = trainerService.getTrainerInfo(userId, trainerId);
        return RestResponse.ok(trainerInfoResponse);
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TrainerResponse> createTrainer(CreateTrainerRequest createTrainerRequest) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Creating trainer: {} userId: {}", createTrainerRequest.getName(), userId);

        SaveTrainerCommand command = new SaveTrainerCommand(createTrainerRequest.getName());
        TrainerEntity trainer = trainerService.createTrainer(command, userId);

        return RestResponse.status(Response.Status.CREATED, toTrainerResponse(trainer));
    }

    private TrainerResponse toTrainerResponse(TrainerEntity trainer) {
        return TrainerResponse.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .createdAt(trainer.getCreatedAt())
                .createdBy(trainer.getCreatedBy())
                .build();
    }

    @Override
    @Authenticated
    public RestResponse<TaskResponse> getTaskWithAttempt(UUID trainerId, UUID taskId) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Get task: userId={}, trainerId={}, taskId={}", userId, trainerId, taskId);
        TaskResponse taskResponse = taskService.getTaskWithAttempt(userId, trainerId, taskId);
        return RestResponse.ok(taskResponse);
    }

    @Override
    @Authenticated
    public RestResponse<MessageResponse> submitTaskAttempt(UUID trainerId, UUID taskId, AnswerRequest request) {
        UUID userId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Submit task attempt: userId={}, trainerId={}, taskId={}", userId, trainerId, taskId);
        SubmissionCheckResult checkResult = taskAttemptService.submitTaskAttempt(userId, trainerId, taskId,
                request.getUserAnswer());

        return RestResponse.ok(MessageResponse.withMessage(checkResult.getMessage()));
    }
}
