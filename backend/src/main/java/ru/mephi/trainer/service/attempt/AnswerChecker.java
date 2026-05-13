package ru.mephi.trainer.service.attempt;

import ru.mephi.trainer.models.attempt.answer.UserAnswer;
import ru.mephi.trainer.models.taskconfig.TaskConfig;

public interface AnswerChecker<C extends TaskConfig, A extends UserAnswer> {
    boolean check(A answer, C config);
    boolean requiresReview();
}
