package ru.mephi.trainer.service.attempt.checker;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.attempt.answer.MultipleChoiceAnswer;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;
import ru.mephi.trainer.service.attempt.AnswerChecker;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class MultipleChoiceChecker implements AnswerChecker<MultipleChoiceConfig, MultipleChoiceAnswer> {

    @Override
    public boolean check(MultipleChoiceAnswer answer, MultipleChoiceConfig config) {
        Set<Integer> expectedOrdinals = new HashSet<>(config.getExpectedOrdinals());
        return answer.getSelectedOrdinals().equals(expectedOrdinals);
    }

    @Override
    public boolean requiresReview() {
        return false;
    }
}
