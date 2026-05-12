package ru.mephi.trainer.rest.api;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.request.auth.LoginRequest;
import ru.mephi.trainer.rest.dto.request.auth.RegistrationRequest;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.auth.LoginResponse;
import ru.mephi.trainer.rest.dto.response.auth.RegistrationResponse;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Аутентификация", description = "Эндпоинты для регистрации и входа в систему")
public interface AuthApi {

    @POST
    @Path("/register")
    @Operation(
            operationId = "registerUser",
            summary = "Регистрация пользователя",
            description = "Создание новой учетной записи пользователя"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Пользователь успешно зарегистрирован",
                    content = @Content(schema = @Schema(implementation = RegistrationResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "409",
                    description = "Пользователь с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<RegistrationResponse> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest);

    @POST
    @Path("/login")
    @Operation(
            operationId = "loginUser",
            summary = "Вход в систему",
            description = "Аутентификация пользователя и получение JWT токена"
    )
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Успешный вход",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Неверный email или пароль",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Неожиданная ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    RestResponse<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest);
}
