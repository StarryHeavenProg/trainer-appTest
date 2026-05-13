package ru.mephi.trainer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.command.SaveTaskCommand;
import ru.mephi.trainer.models.taskconfig.AnswerChoice;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.rest.dto.request.task.AnswerChoiceDto;
import ru.mephi.trainer.rest.dto.request.task.ErrorFindingConfigRequestDto;
import ru.mephi.trainer.rest.dto.request.task.MultipleChoiceConfigRequestDto;
import ru.mephi.trainer.rest.dto.request.task.OpenAnswerConfigRequestDto;
import ru.mephi.trainer.rest.dto.request.task.SaveTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.SingleChoiceConfigRequestDto;
import ru.mephi.trainer.rest.dto.request.task.TaskConfigRequestDto;
import ru.mephi.trainer.rest.dto.response.task.admin.TaskAdminResponse;
import ru.mephi.trainer.rest.dto.response.task.admin.config.ErrorFindingConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.MultipleChoiceConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.OpenAnswerConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.SingleChoiceConfigDto;
import ru.mephi.trainer.rest.dto.response.task.admin.config.TaskConfigDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskMapper {

    private final ObjectMapper objectMapper;

    public SaveTaskCommand toCommand(SaveTaskRequest request) {
        if (request == null) {
            return null;
        }

        return SaveTaskCommand.builder()
                .trainerIds(request.getTrainerIds())
                .config(toTaskConfig(request.getConfig()))
                .build();
    }

    public TaskAdminResponse toTaskAdminResponse(TaskEntity task) {
        if (task == null) {
            return null;
        }

        return TaskAdminResponse.builder()
                .id(task.getId())
                .taskType(task.getTaskType())
                .config(mapConfigToDto(task.getConfig(), task.getTaskType()))
                .createdBy(task.getCreatedBy())
                .createdAt(task.getCreatedAt())
                .trainerIds(extractTrainerIds(task))
                .build();
    }

    private Set<UUID> extractTrainerIds(TaskEntity task) {
        if (task.getTrainerLinks() == null || task.getTrainerLinks().isEmpty()) {
            return Set.of();
        }

        return task.getTrainerLinks().stream()
                .map(link -> link.getTrainer().getId())
                .collect(Collectors.toSet());
    }

    private TaskConfigDto mapConfigToDto(String configJson, TaskType taskType) {
        if (configJson == null || configJson.isBlank()) {
            return null;
        }

        try {
            return switch (taskType) {
                case SINGLE_CHOICE -> toSingleChoiceConfigDto(
                        objectMapper.readValue(configJson, SingleChoiceConfig.class)
                );
                case MULTIPLE_CHOICE -> toMultipleChoiceConfigDto(
                        objectMapper.readValue(configJson, MultipleChoiceConfig.class)
                );
                case ERROR_FINDING -> toErrorFindingConfigDto(
                        objectMapper.readValue(configJson, ErrorFindingConfig.class)
                );
                case OPEN_ANSWER -> toOpenAnswerConfigDto(
                        objectMapper.readValue(configJson, OpenAnswerConfig.class)
                );
                default -> throw new IllegalArgumentException("Unsupported task type: " + taskType);
            };
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse config JSON for task type: " + taskType, e);
        }
    }

    private SingleChoiceConfigDto toSingleChoiceConfigDto(SingleChoiceConfig config) {
        if (config == null) {
            return null;
        }

        return SingleChoiceConfigDto.builder()
                .question(config.getQuestion())
                .answerChoices(toAnswerChoiceDtoList(config.getAnswerChoices()))
                .expectedOrdinal(config.getExpectedOrdinal())
                .points(config.getPoints())
                .mistakeCost(config.getMistakeCost())
                .maxAttempts(config.getMaxAttempts())
                .build();
    }

    private MultipleChoiceConfigDto toMultipleChoiceConfigDto(MultipleChoiceConfig config) {
        if (config == null) {
            return null;
        }

        return MultipleChoiceConfigDto.builder()
                .question(config.getQuestion())
                .answerChoices(toAnswerChoiceDtoList(config.getAnswerChoices()))
                .expectedOrdinals(config.getExpectedOrdinals())
                .points(config.getPoints())
                .mistakeCost(config.getMistakeCost())
                .maxAttempts(config.getMaxAttempts())
                .build();
    }

    private ErrorFindingConfigDto toErrorFindingConfigDto(ErrorFindingConfig config) {
        if (config == null) {
            return null;
        }

        return ErrorFindingConfigDto.builder()
                .question(config.getQuestion())
                .answer(config.getAnswer())
                .points(config.getPoints())
                .mistakeCost(config.getMistakeCost())
                .maxAttempts(config.getMaxAttempts())
                .build();
    }

    private OpenAnswerConfigDto toOpenAnswerConfigDto(OpenAnswerConfig config) {
        if (config == null) {
            return null;
        }

        return OpenAnswerConfigDto.builder()
                .question(config.getQuestion())
                .context(config.getContext())
                .expectedAnswer(config.getExpectedAnswer())
                .points(config.getPoints())
                .mistakeCost(config.getMistakeCost())
                .maxAttempts(config.getMaxAttempts())
                .build();
    }


    private TaskConfig toTaskConfig(TaskConfigRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return switch (dto) {
            case SingleChoiceConfigRequestDto sc -> mapSingleChoiceConfig(sc);
            case MultipleChoiceConfigRequestDto mc -> mapMultipleChoiceConfig(mc);
            case ErrorFindingConfigRequestDto ef -> mapErrorFindingConfig(ef);
            case OpenAnswerConfigRequestDto oa -> mapOpenAnswerConfig(oa);
            default -> throw new IllegalArgumentException("Unsupported config type: " + dto.getClass().getSimpleName());
        };
    }

    private SingleChoiceConfig mapSingleChoiceConfig(SingleChoiceConfigRequestDto dto) {
        return SingleChoiceConfig.builder()
                .question(dto.getQuestion())
                .answerChoices(toAnswerChoices(dto.getAnswerChoices()))
                .expectedOrdinal(dto.getExpectedOrdinal())
                .points(dto.getPoints())
                .mistakeCost(dto.getMistakeCost())
                .maxAttempts(dto.getMaxAttempts())
                .build();
    }

    private MultipleChoiceConfig mapMultipleChoiceConfig(MultipleChoiceConfigRequestDto dto) {
        return MultipleChoiceConfig.builder()
                .question(dto.getQuestion())
                .answerChoices(toAnswerChoices(dto.getAnswerChoices()))
                .expectedOrdinals(dto.getExpectedOrdinals())
                .points(dto.getPoints())
                .mistakeCost(dto.getMistakeCost())
                .maxAttempts(dto.getMaxAttempts())
                .build();
    }

    private ErrorFindingConfig mapErrorFindingConfig(ErrorFindingConfigRequestDto dto) {
        return ErrorFindingConfig.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .points(dto.getPoints())
                .mistakeCost(dto.getMistakeCost())
                .maxAttempts(dto.getMaxAttempts())
                .build();
    }

    private OpenAnswerConfig mapOpenAnswerConfig(OpenAnswerConfigRequestDto dto) {
        return OpenAnswerConfig.builder()
                .question(dto.getQuestion())
                .context(dto.getContext())
                .expectedAnswer(dto.getExpectedAnswer())
                .points(dto.getPoints())
                .mistakeCost(dto.getMistakeCost())
                .maxAttempts(dto.getMaxAttempts())
                .build();
    }

    private AnswerChoice toAnswerChoice(AnswerChoiceDto dto) {
        if (dto == null) {
            return null;
        }
        return new AnswerChoice(dto.getOrdinal(), dto.getChoice());
    }

    private List<AnswerChoice> toAnswerChoices(List<AnswerChoiceDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toAnswerChoice)
                .toList();
    }

    private List<AnswerChoiceDto> toAnswerChoiceDtoList(java.util.List<AnswerChoice> choices) {
        if (choices == null) {
            return List.of();
        }

        return choices.stream()
                .map(this::toAnswerChoiceDto)
                .toList();
    }

    private AnswerChoiceDto toAnswerChoiceDto(AnswerChoice choice) {
        if (choice == null) {
            return null;
        }

        return new AnswerChoiceDto(choice.getOrdinal(), choice.getChoice());
    }
}
