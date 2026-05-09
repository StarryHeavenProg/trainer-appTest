package ru.mephi.trainer.rest.controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.rest.api.TestApi;
import ru.mephi.trainer.rest.dto.response.test.MessageResponse;
import ru.mephi.trainer.rest.dto.response.test.UserInfoResponse;
import ru.mephi.trainer.service.CurrentUserService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TestController implements TestApi {

    private final JsonWebToken jwt;
    private final CurrentUserService currentUserService;

    @Override
    @PermitAll
    public RestResponse<MessageResponse> publicEndpoint() {
        log.info("Public endpoint accessed");

        return RestResponse.ok(MessageResponse.builder()
                .message("Это публичный эндпоинт. Доступен всем, даже без токена!")
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @Authenticated
    public RestResponse<MessageResponse> authenticatedOnly() {
        String email = jwt.getName();
        Optional<UUID> userId = currentUserService.getCurrentUserId();

        log.info("Authenticated-only endpoint accessed by: {}", email);

        String message = userId
                .map(id -> String.format("Доступ разрешен! Вы авторизованы как: %s (ID: %s)", email, id))
                .orElse(String.format("Доступ разрешен! Вы авторизованы как: %s", email));

        return RestResponse.ok(MessageResponse.builder()
                .message(message)
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @RolesAllowed({"student"})
    public RestResponse<MessageResponse> studentOnly() {
        log.info("Student endpoint accessed by: {}", jwt.getName());

        return RestResponse.ok(MessageResponse.builder()
                .message("Привет, студент! Ты имеешь доступ к учебным материалам.")
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @RolesAllowed({"expert"})
    public RestResponse<MessageResponse> expertOnly() {
        log.info("Expert endpoint accessed by: {}", jwt.getName());

        return RestResponse.ok(MessageResponse.builder()
                .message("Здравствуйте, эксперт! Вы можете проверять задания.")
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @RolesAllowed({"admin"})
    public RestResponse<MessageResponse> adminOnly() {
        log.info("Admin endpoint accessed by: {}", jwt.getName());

        return RestResponse.ok(MessageResponse.builder()
                .message("Добро пожаловать, администратор! У вас полный доступ.")
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @RolesAllowed({"student", "expert"})
    public RestResponse<MessageResponse> studentOrExpert() {
        String role = getRoleFromJwt();
        log.info("Student/Expert endpoint accessed by: {} with role: {}",
                jwt.getName(), role);

        return RestResponse.ok(MessageResponse.builder()
                .message(String.format("Доступ разрешен! Ваша роль: %s. Эта зона для студентов и экспертов.", role))
                .timestamp(Instant.now().toString())
                .build());
    }

    @Override
    @Authenticated
    public RestResponse<UserInfoResponse> getCurrentUserInfo() {
        String email = jwt.getName();
        String role = getRoleFromJwt();
        String userId = jwt.getClaim("userId");

        log.info("User info requested for: {}", email);

        return RestResponse.ok(UserInfoResponse.builder()
                .email(email)
                .role(role)
                .userId(UUID.fromString(userId))
                .build());
    }

    private String getRoleFromJwt() {
        return jwt.getGroups().stream()
                .findFirst()
                .orElse("UNKNOWN");
    }
}