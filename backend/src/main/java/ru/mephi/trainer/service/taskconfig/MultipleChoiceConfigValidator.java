package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.models.taskconfig.AnswerChoice;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class MultipleChoiceConfigValidator {

    private final AnswerChoiceValidator answerChoiceValidator;

    public Set<String> validate(MultipleChoiceConfig config) {
        Set<String> answerChoicesErrors = answerChoiceValidator.validate(config.getAnswerChoices());
        Set<String> errors = new HashSet<>(answerChoicesErrors);

        if (errors.contains(AnswerChoiceValidator.EMPTY_ANSWER_CHOICES_ERROR)) {
            return errors;
        }

        Set<Integer> ordinals = config.getAnswerChoices().stream()
                .map(AnswerChoice::getOrdinal)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        validateExpectedOrdinals(config.getExpectedOrdinals(), ordinals, errors);

        if (config.getExpectedOrdinals() != null
                && config.getExpectedOrdinals().size() > 1
                && config.getExpectedOrdinals().size() == config.getAnswerChoices().size()) {
            errors.add("Не все варианты ответа могут быть правильными. Выберите хотя бы один неправильный вариант");
        }

        return errors;
    }

    private void validateExpectedOrdinals(List<Integer> expectedOrdinals, Set<Integer> availableOrdinals, Set<String> errors) {
        if (expectedOrdinals == null || expectedOrdinals.isEmpty()) {
            errors.add("Ожидаемые ответы (expectedOrdinals) не могут быть пустыми");
            return;
        }

        Set<Integer> uniqueOrdinals = new HashSet<>();
        for (Integer ordinal : expectedOrdinals) {
            if (ordinal == null) {
                errors.add("Ordinal ожидаемого ответа не может быть null");
            } else if (!uniqueOrdinals.add(ordinal)) {
                errors.add(String.format("Ordinal '%d' повторяется в списке ожидаемых ответов", ordinal));
            }
        }

        for (Integer ordinal : uniqueOrdinals) {
            if (!availableOrdinals.contains(ordinal)) {
                errors.add(String.format(
                        "Ожидаемый ответ с ordinal '%d' не существует среди вариантов ответа. Доступные ordinal: %s",
                        ordinal, availableOrdinals
                ));
            }
        }
    }
}
