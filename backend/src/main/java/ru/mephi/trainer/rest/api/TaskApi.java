package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
import ru.mephi.trainer.rest.dto.request.task.SaveTaskRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.task.admin.TaskAdminResponse;

import java.util.UUID;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Задания", description = "Управление заданиями")
public interface TaskApi {

    @POST
    @Path("/")
    @Operation(
            operationId = "createTask",
            summary = "Создание задания",
            description = "Создать задание"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Задание успешно создано",
                    content = @Content(schema = @Schema(implementation = TaskAdminResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
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
    RestResponse<TaskAdminResponse> createTask(@RequestBody @Valid SaveTaskRequest request);

    @PUT
    @Path("/{id}")
    @Operation(
            operationId = "updateTask",
            summary = "Обновление задания",
            description = "Обновить существующее задание"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Задание успешно обновлено",
                    content = @Content(schema = @Schema(implementation = TaskAdminResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные параметры запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Доступ запрещён",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Задание или тренажёр не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TaskAdminResponse> updateTask(
            @PathParam("id") UUID id,
            @RequestBody @Valid SaveTaskRequest request
    );
}
