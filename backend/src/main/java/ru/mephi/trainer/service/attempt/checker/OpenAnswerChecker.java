package ru.mephi.trainer.service.attempt.checker;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.attempt.answer.OpenAnswer;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;
import ru.mephi.trainer.service.attempt.AnswerChecker;

@ApplicationScoped
public class OpenAnswerChecker implements AnswerChecker<OpenAnswerConfig, OpenAnswer> {

    @Override
    public boolean check(OpenAnswer answer, OpenAnswerConfig config) {
        return false;
    }

    @Override
    public boolean requiresReview() {
        return true;
    }
}
