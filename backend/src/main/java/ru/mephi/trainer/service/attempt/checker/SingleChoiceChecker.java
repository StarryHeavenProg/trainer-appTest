package ru.mephi.trainer.service.attempt.checker;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.attempt.answer.SingleChoiceAnswer;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;
import ru.mephi.trainer.service.attempt.AnswerChecker;

@ApplicationScoped
public class SingleChoiceChecker implements AnswerChecker<SingleChoiceConfig, SingleChoiceAnswer> {

    @Override
    public boolean check(SingleChoiceAnswer answer, SingleChoiceConfig config) {
        return answer.getSelectedOrdinal().equals(config.getExpectedOrdinal());
    }

    @Override
    public boolean requiresReview() {
        return false;
    }
}
