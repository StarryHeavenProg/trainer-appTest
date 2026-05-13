package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
public class OpenAnswerConfigValidator {

    public Set<String> validate(OpenAnswerConfig config) {
        Set<String> errors = new HashSet<>();
        if (isBlank(config.getExpectedAnswer())) {
            errors.add("Ожидаемый ответ не может быть пустым");
        }

        return errors;
    }
}
