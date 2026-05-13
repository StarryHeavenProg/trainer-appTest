package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.ExpertApi;
import ru.mephi.trainer.rest.dto.request.SubmitReviewRequest;
import ru.mephi.trainer.rest.dto.response.task.expert.AnswerTaskResponse;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.rest.dto.response.task.expert.ReviewTaskResponse;
import ru.mephi.trainer.service.ExpertService;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ExpertController implements ExpertApi {

    private final ExpertService expertService;

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<List<ReviewTaskResponse>> getListExamination() {
        log.info("Expert endpoint list examination accessed");
        List<ReviewTaskResponse> response = expertService.getReviewTask();
        return RestResponse.ok(response);
    }

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<AnswerTaskResponse> getTaskForCheck(UUID taskAttemptId) {
        log.info("Expert endpoint get task for check accessed: id={}", taskAttemptId);
        AnswerTaskResponse response = expertService.getTaskForCheck(taskAttemptId);
        return RestResponse.ok(response);
    }

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<MessageResponse> setPointForTask(UUID taskAttemptId, SubmitReviewRequest request) {
        log.info("Expert endpoint set task point accessed: id={}, correct={}", taskAttemptId, request.getIsCorrect());
        int points = expertService.setPointsForTask(taskAttemptId, request.getIsCorrect());

        MessageResponse response = MessageResponse.withMessage("Работа оценена. Поставлено баллов: " + points);

        return RestResponse.ok(response);
    }
}