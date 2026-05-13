package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.request.AnswerRequest;
import ru.mephi.trainer.rest.dto.request.trainer.CreateTrainerRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.MessageResponse;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerInfoResponse;
import ru.mephi.trainer.rest.dto.response.trainer.TrainerResponse;


import java.util.List;
import java.util.UUID;

@Path("/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Тренажеры", description = "Эндпоинты для тренажера")
public interface TrainersAPI {

    @GET
    @Path("/")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            operationId = "getTrainers",
            summary = "Список тренажеров",
            description = "Получить информацию о тренажерах"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о списке тренажеров успешно получены",
                    content = @Content(schema = @Schema(implementation = TrainerResponse[].class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<List<TrainerResponse>> getTrainers();

    @GET
    @Path("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            operationId = "getTrainerInfo",
            summary = "Информация о тренажере",
            description = "Получить информацию о тренажере"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о тренажере успешно получены",
                    content = @Content(schema = @Schema(implementation = TrainerInfoResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TrainerInfoResponse> getTrainerInfo(@PathParam("id") UUID trainerId);

    @POST
    @Path("/")
    @Operation(
            operationId = "createTrainer",
            summary = "Создание тренажера",
            description = "Создать новый тренажер"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Тренажер успешно создан",
                    content = @Content(schema = @Schema(implementation = TrainerInfoResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TrainerResponse> createTrainer(@RequestBody @Valid CreateTrainerRequest createTrainerRequest);

    @GET
    @Path("/{trainer-id}/tasks/{task-id}")
    @Operation(
            operationId = "getTaskWithAttempt",
            summary = "Информация о задаче",
            description = "Получить информацию о задаче с учётом последней попытки пользователя"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о задаче успешно получены",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))
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
                    description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskResponse> getTaskWithAttempt(
            @PathParam("trainer-id") UUID trainerId,
            @PathParam("task-id") UUID taskId
    );

    @POST
    @Path("/{trainer-id}/tasks/{task-id}/submit")
    @Operation(
            operationId = "submitTaskAttempt",
            summary = "Отправить ответ на задание",
            description = "Отправить ответ на задание"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Ответ отправлен",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ошибка при отправке ответа",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Неверный ответ",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<MessageResponse> submitTaskAttempt(@PathParam("trainer-id") UUID trainerId, @PathParam("task-id") UUID taskId, @Valid AnswerRequest request);

}
