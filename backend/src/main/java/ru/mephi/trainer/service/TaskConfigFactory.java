package ru.mephi.trainer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.TaskConfig;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class TaskConfigFactory {
    private final ObjectMapper objectMapper;

    public TaskConfig createConfig(String configJson, TaskType taskType) {
        try {
            return switch (taskType) {
                case SINGLE_CHOICE -> objectMapper.readValue(configJson, SingleChoiceConfig.class);
                case MULTIPLE_CHOICE -> objectMapper.readValue(configJson, MultipleChoiceConfig.class);
                case OPEN_ANSWER -> objectMapper.readValue(configJson, OpenAnswerConfig.class);
                case ERROR_FINDING -> objectMapper.readValue(configJson, ErrorFindingConfig.class);
            };
        } catch (JsonProcessingException e) {
            log.error("Ошибка десериализации конфига задачи: {}", e.getMessage());
            throw new IllegalArgumentException("Неверный формат конфигурации задачи", e);
        }
    }
}
