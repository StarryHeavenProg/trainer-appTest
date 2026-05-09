package ru.mephi.trainer.rest.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;
import ru.mephi.trainer.rest.dto.response.test.MessageResponse;
import ru.mephi.trainer.rest.dto.response.test.UserInfoResponse;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "RBAC", description = "Эндпоинты для проверки доступа по ролям")
public interface TestApi {

    @GET
    @Path("/public")
    @Operation(operationId = "publicEndpoint", summary = "Публичный доступ", description = "Доступен без аутентификации")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    RestResponse<MessageResponse> publicEndpoint();

    @GET
    @Path("/authenticated")
    @Operation(
            operationId = "authenticatedOnly",
            summary = "Только для аутентифицированных пользователей",
            description = "Доступен любым аутентифицированным пользователям (student, expert, admin)"
    )
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    @APIResponse(responseCode = "401", description = "Требуется аутентификация", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RestResponse<MessageResponse> authenticatedOnly();

    @GET
    @Path("/student")
    @Operation(operationId = "studentOnly", summary = "Только для студентов", description = "Доступен только пользователям с ролью student")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    @APIResponse(responseCode = "401", description = "Не аутентифицирован", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "403", description = "Недостаточно прав", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RestResponse<MessageResponse> studentOnly();

    @GET
    @Path("/expert")
    @Operation(operationId = "expertOnly", summary = "Только для экспертов", description = "Доступен только пользователям с ролью expert")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    @APIResponse(responseCode = "401", description = "Не аутентифицирован", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "403", description = "Недостаточно прав", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RestResponse<MessageResponse> expertOnly();

    @GET
    @Path("/admin")
    @Operation(operationId = "adminOnly", summary = "Только для администраторов", description = "Доступен только пользователям с ролью admin")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    @APIResponse(responseCode = "401", description = "Не аутентифицирован", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "403", description = "Недостаточно прав", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    RestResponse<MessageResponse> adminOnly();

    @GET
    @Path("/student-or-expert")
    @Operation(operationId = "studentOrExpert", summary = "Для студентов и экспертов", description = "Доступен пользователям с ролью student или expert")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    RestResponse<MessageResponse> studentOrExpert();

    @GET
    @Path("/me")
    @Operation(operationId = "getCurrentUserInfo", summary = "Информация о текущем пользователе", description = "Показывает данные из JWT токена")
    @SecurityRequirement(name = "bearerAuth")
    @APIResponse(responseCode = "200", description = "Успешный доступ")
    RestResponse<UserInfoResponse> getCurrentUserInfo();
}
