package ru.mephi.trainer.rest.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.mapper.TaskMapper;
import ru.mephi.trainer.models.command.SaveTaskCommand;
import ru.mephi.trainer.rest.api.TaskApi;
import ru.mephi.trainer.rest.dto.request.task.SaveTaskRequest;
import ru.mephi.trainer.rest.dto.response.task.admin.TaskAdminResponse;
import ru.mephi.trainer.service.CurrentUserService;
import ru.mephi.trainer.service.TaskService;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;
    private final CurrentUserService currentUserService;
    private final TaskMapper taskMapper;

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> createTask(SaveTaskRequest request) {
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Creating task: {}", request.getTaskType());

        SaveTaskCommand command = taskMapper.toCommand(request);

        TaskEntity task = taskService.createTask(command, currentUserId);

        TaskAdminResponse response = taskMapper.toTaskAdminResponse(task);

        return RestResponse.status(RestResponse.Status.CREATED, response);
    }

    @Override
    @RolesAllowed({"expert", "admin"})
    public RestResponse<TaskAdminResponse> updateTask(UUID id, SaveTaskRequest request) {
        UUID currentUserId = currentUserService.getCurrentUserIdOrThrow();
        log.info("Updating task: id: {} type: {} userId: {}", id, request.getTaskType(), currentUserId);

        SaveTaskCommand command = taskMapper.toCommand(request);
        TaskEntity task = taskService.updateTask(id, command);

        TaskAdminResponse response = taskMapper.toTaskAdminResponse(task);

        return RestResponse.ok(response);
    }
}
