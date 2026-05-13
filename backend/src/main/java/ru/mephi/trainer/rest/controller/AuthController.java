package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.rest.api.AuthApi;
import ru.mephi.trainer.rest.dto.request.auth.LoginRequest;
import ru.mephi.trainer.rest.dto.request.auth.RegistrationRequest;
import ru.mephi.trainer.rest.dto.response.auth.LoginResponse;
import ru.mephi.trainer.rest.dto.response.auth.RegistrationResponse;
import ru.mephi.trainer.service.AuthService;
import ru.mephi.trainer.util.SecurityUtil;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PermitAll
    public RestResponse<RegistrationResponse> registerUser(RegistrationRequest registrationRequest) {
        log.info("Registering user");

        UserEntity registeredUser = authService.register(registrationRequest);
        String token = authService.generateToken(registeredUser);

        return RestResponse.status(RestResponse.Status.CREATED, RegistrationResponse.builder()
                .message("Вы успешно зарегистрированы")
                .token(token)
                .build());
    }

    @Override
    @PermitAll
    public RestResponse<LoginResponse> loginUser(LoginRequest loginRequest) {
        log.info("Login attempt");

        UserEntity authenticatedUser = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = authService.generateToken(authenticatedUser);

        return RestResponse.ok(LoginResponse.builder()
                .token(token)
                .email(authenticatedUser.getEmail())
                .role(SecurityUtil.toSecurityRole(authenticatedUser.getUserRole()))
                .build());
    }
}
