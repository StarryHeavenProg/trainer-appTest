package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.request.SubmitReviewRequest;
import ru.mephi.trainer.rest.dto.response.task.expert.AnswerTaskResponse;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.rest.dto.response.task.expert.ReviewTaskResponse;

import java.util.List;
import java.util.UUID;

@Path("/expert")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Эксперт", description = "Эндпоинты для эксперта")
public interface ExpertApi {

    @GET
    @Path("/examination")
    @Operation(
            operationId = "getListExamination",
            summary = "Список проверяемых задач",
            description = "Получить информацию о задачах, которые необходимо проверить"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные успешно получены",
                    content = @Content(schema = @Schema(implementation = ReviewTaskResponse[].class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Нет прав",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<List<ReviewTaskResponse>> getListExamination();

    @GET
    @Path("/examination/{id}")
    @Operation(
            operationId = "getExamination",
            summary = "Информация о решении задачи",
            description = "Получить ответ пользователя к задаче, которую необходимо проверить"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о прохождении тренажера успешно получены",
                    content = @Content(schema = @Schema(implementation = AnswerTaskResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Нет прав",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ответ пользователя не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<AnswerTaskResponse> getTaskForCheck(@PathParam("id") UUID taskAttemptId);

    @POST
    @Path("/examination/{id}")
    @Operation(
            operationId = "setPointForTask",
            summary = "Выставить балл",
            description = "Выставить балл за проверяемое задание"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Балл был успешно выставлен",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Нет прав",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ответ пользователя не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<MessageResponse> setPointForTask(
            @PathParam("id") UUID taskAttemptId,
            @Valid SubmitReviewRequest request);
}
