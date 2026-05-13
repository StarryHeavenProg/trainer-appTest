package ru.mephi.trainer.service.attempt;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.attempt.answer.UserAnswer;
import ru.mephi.trainer.models.taskconfig.TaskConfig;
import ru.mephi.trainer.service.attempt.checker.ErrorFindingChecker;
import ru.mephi.trainer.service.attempt.checker.MultipleChoiceChecker;
import ru.mephi.trainer.service.attempt.checker.OpenAnswerChecker;
import ru.mephi.trainer.service.attempt.checker.SingleChoiceChecker;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AnswerCheckerRegistry {
    private final Map<TaskType, AnswerChecker<?, ?>> checkers = new EnumMap<>(TaskType.class);
    private final Instance<AnswerChecker<?, ?>> checkerInstance;

    @PostConstruct
    public void init() {
        for (AnswerChecker<?, ?> checker : checkerInstance) {
            switch (checker) {
                case SingleChoiceChecker singleChoiceChecker -> checkers.put(TaskType.SINGLE_CHOICE, checker);
                case MultipleChoiceChecker multipleChoiceChecker -> checkers.put(TaskType.MULTIPLE_CHOICE, checker);
                case OpenAnswerChecker openAnswerChecker -> checkers.put(TaskType.OPEN_ANSWER, checker);
                case ErrorFindingChecker errorFindingChecker -> checkers.put(TaskType.ERROR_FINDING, checker);
                default -> throw new IllegalStateException("Unexpected value: " + checker);
            }
        }
        validateAllCheckersRegistered();
    }

    @SuppressWarnings("unchecked")
    public <C extends TaskConfig, A extends UserAnswer> AnswerChecker<C, A> getChecker(TaskType taskType) {
        AnswerChecker<?, ?> checker = checkers.get(taskType);
        if (checker == null) {
            throw new IllegalArgumentException("Неподдерживаемый тип задачи: " + taskType);
        }
        return (AnswerChecker<C, A>) checker;
    }

    private void validateAllCheckersRegistered() {
        for (TaskType type : TaskType.values()) {
            if (!checkers.containsKey(type)) {
                throw new IllegalStateException(
                        String.format("Missing checker for task type: %s. " +
                                "Please implement AnswerChecker for this task type.", type)
                );
            }
        }

        log.info("Successfully registered checkers for all task types: {}", checkers.keySet());
    }
}
