package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.exception.InvalidCommandDataException;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.TaskConfig;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
@RequiredArgsConstructor
public class TaskConfigValidator {

    private final OpenAnswerConfigValidator openAnswerValidator;
    private final ErrorFindingConfigValidator errorFindingValidator;
    private final SingleChoiceConfigValidator singleChoiceValidator;
    private final MultipleChoiceConfigValidator multipleChoiceValidator;

    public Set<String> validateConfig(TaskConfig config) {
        if (config == null) {
            throw new InvalidCommandDataException("Task config can not be null");
        }
        Set<String> errors = new HashSet<>();

        if (config.getMaxAttempts() < 1) {
            errors.add("Кол-во попыток не может быть меньше 1");
        }
        if (isBlank(config.getQuestion())) {
            errors.add("Вопрос не может быть пустым");
        }
        Set<String> specificConfigErrors = switch (config.getTaskType()) {
            case OPEN_ANSWER -> openAnswerValidator.validate((OpenAnswerConfig) config);
            case ERROR_FINDING -> errorFindingValidator.validate((ErrorFindingConfig) config);
            case SINGLE_CHOICE -> singleChoiceValidator.validate((SingleChoiceConfig) config);
            case MULTIPLE_CHOICE -> multipleChoiceValidator.validate((MultipleChoiceConfig) config);
        };

        errors.addAll(specificConfigErrors);

        return errors;
    }
}
