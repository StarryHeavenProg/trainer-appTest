package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
public class ErrorFindingConfigValidator {

    public Set<String> validate(ErrorFindingConfig config) {
        Set<String> errors = new HashSet<>();
        if (isBlank(config.getAnswer())) {
            errors.add("Ответ не может быть пустым");
        }

        return errors;
    }
}
