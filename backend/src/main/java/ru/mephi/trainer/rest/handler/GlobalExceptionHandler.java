package ru.mephi.trainer.rest.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import ru.mephi.trainer.exception.*;
import ru.mephi.trainer.rest.dto.response.ErrorResponse;

@Slf4j
@Provider
public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception: ", e);

        ErrorResponse response = ErrorResponse.builder()
                .message("Упс! Что-то пошло не так")
                .build();

        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleException(FailedLoginException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message("Неверный email или пароль")
                .build();

        return RestResponse.status(Response.Status.UNAUTHORIZED, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleException(EmailAlreadyExistsException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message("Этот email уже занят")
                .build();

        return RestResponse.status(Response.Status.BAD_REQUEST, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return RestResponse.status(Response.Status.NOT_FOUND, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleLastTaskAttemptStatusNotFailedException(LastTaskAttemptStatusNotFailedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return RestResponse.status(Response.Status.CONFLICT, response);
    }


    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleUserUseMaxAttemptsLimitException(UserUseMaxAttemptsLimitException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return RestResponse.status(Response.Status.CONFLICT, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleStatusTaskNotReviewException(StatusTaskNotReviewException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .build();

        return RestResponse.status(Response.Status.CONFLICT, response);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> handleInvalidCommandDataException(InvalidCommandDataException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();

        return RestResponse.status(Response.Status.BAD_REQUEST, response);
    }
}
