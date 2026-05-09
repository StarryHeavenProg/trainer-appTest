package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.rest.api.AuthApi;
import ru.mephi.trainer.rest.dto.request.LoginRequest;
import ru.mephi.trainer.rest.dto.request.RegistrationRequest;
import ru.mephi.trainer.rest.dto.response.LoginResponse;
import ru.mephi.trainer.rest.dto.response.RegistrationResponse;
import ru.mephi.trainer.service.AuthService;

@Slf4j
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PermitAll
    public RestResponse<RegistrationResponse> registerUser(RegistrationRequest registrationRequest) {
        log.info("Registering user with email: {}", registrationRequest.getEmail());

        UserEntity registeredUser = authService.register(registrationRequest);
        String token = authService.generateToken(registeredUser);

        log.info("User registered successfully: {}", registeredUser.getEmail());

        return RestResponse.status(RestResponse.Status.CREATED, RegistrationResponse.builder()
                .message("Вы успешно зарегистрированы")
                .token(token)
                .build());
    }

    @Override
    @PermitAll
    public RestResponse<LoginResponse> loginUser(LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        UserEntity authenticatedUser = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = authService.generateToken(authenticatedUser);

        log.info("User logged in successfully: {}", authenticatedUser.getEmail());

        return RestResponse.ok(LoginResponse.builder()
                .token(token)
                .email(authenticatedUser.getEmail())
                .role(authenticatedUser.getUserRole().getSecurityRole())
                .build());
    }
}
