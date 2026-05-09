package ru.mephi.trainer.rest.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.response.ProfileResponse;
import ru.mephi.trainer.rest.dto.response.TrainerProgressResponse;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;

import java.util.UUID;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Профиль", description = "Эндпоинты для профиля")
public interface ProfileApi {

    @GET
    @Path("/")
    @Operation(
            operationId = "getProfile",
            summary = "Профиль пользователя",
            description = "Получить информацию о пользователе"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные пользователя успешно получены",
                    content = @Content(schema = @Schema(implementation = ProfileResponse.class))
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
    RestResponse<ProfileResponse> getProfile();

    @GET
    @Path("/trainers/{id}")
    @Operation(
            operationId = "getProfileTrainer",
            summary = "Информация о прохождении тренажера",
            description = "Получить информацию о статусе прохождения тренажера"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Данные о прохождении тренажера успешно получены",
                    content = @Content(schema = @Schema(implementation = TrainerProgressResponse.class))
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
                    description = "Тренажёр не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<TrainerProgressResponse> getTrainerProgress(@PathParam("id") UUID trainerId);
}
