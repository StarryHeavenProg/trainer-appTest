package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.models.taskconfig.AnswerChoice;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class SingleChoiceConfigValidator {

    private final AnswerChoiceValidator answerChoiceValidator;

    public Set<String> validate(SingleChoiceConfig config) {
        Set<String> answerChoicesErrors = answerChoiceValidator.validate(config.getAnswerChoices());
        Set<String> errors = new HashSet<>(answerChoicesErrors);

        if (errors.contains(AnswerChoiceValidator.EMPTY_ANSWER_CHOICES_ERROR)) {
            return errors;
        }

        Set<Integer> ordinals = config.getAnswerChoices().stream()
                .map(AnswerChoice::getOrdinal)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (config.getExpectedOrdinal() == null) {
            errors.add("Ожидаемый ответ (expectedOrdinal) не может быть null");
        } else if (!ordinals.contains(config.getExpectedOrdinal())) {
            errors.add(String.format(
                    "Ожидаемый ответ с ordinal '%d' не существует среди вариантов ответа. Доступные ordinal: %s",
                    config.getExpectedOrdinal(),
                    ordinals
            ));
        }

        return errors;
    }
}
