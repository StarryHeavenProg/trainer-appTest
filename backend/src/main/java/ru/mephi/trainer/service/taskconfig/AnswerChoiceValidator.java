package ru.mephi.trainer.service.taskconfig;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.taskconfig.AnswerChoice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
public class AnswerChoiceValidator {

    public static final String EMPTY_ANSWER_CHOICES_ERROR = "Варианты ответа должны быть заполнены";

    public Set<String> validate(List<AnswerChoice> answerChoices) {
        Set<String> errors = new HashSet<>();
        if (answerChoices == null || answerChoices.isEmpty()) {
            errors.add(EMPTY_ANSWER_CHOICES_ERROR);
            return errors;
        }

        Set<Integer> ordinals = new HashSet<>();
        Set<String> choiceTexts = new HashSet<>();

        for (AnswerChoice choice : answerChoices) {
            if (choice.getOrdinal() == null) {
                errors.add("Ordinal варианта ответа не может быть null");
            } else if (choice.getOrdinal() < 1) {
                errors.add(String.format("Ordinal '%d' не может быть меньше 1", choice.getOrdinal()));
            } else if (!ordinals.add(choice.getOrdinal())) {
                errors.add(String.format("Ordinal '%d' повторяется в вариантах ответа", choice.getOrdinal()));
            }

            if (isBlank(choice.getChoice())) {
                errors.add("Текст варианта ответа не может быть пустым");
            } else if (!choiceTexts.add(choice.getChoice())) {
                errors.add(String.format("Текст варианта ответа '%s' повторяется", choice.getChoice()));
            }
        }

        return errors;
    }
}
