package ru.mephi.trainer.service.attempt.checker;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.attempt.answer.ErrorFindingAnswer;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;
import ru.mephi.trainer.service.attempt.AnswerChecker;

@ApplicationScoped
public class ErrorFindingChecker implements AnswerChecker<ErrorFindingConfig, ErrorFindingAnswer> {

    @Override
    public boolean check(ErrorFindingAnswer answer, ErrorFindingConfig config) {
        return answer.getAnswer().equalsIgnoreCase(config.getAnswer());
    }

    @Override
    public boolean requiresReview() {
        return false;
    }
}
