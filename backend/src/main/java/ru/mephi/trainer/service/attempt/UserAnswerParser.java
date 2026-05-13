package ru.mephi.trainer.service.attempt;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.attempt.answer.ErrorFindingAnswer;
import ru.mephi.trainer.models.attempt.answer.MultipleChoiceAnswer;
import ru.mephi.trainer.models.attempt.answer.OpenAnswer;
import ru.mephi.trainer.models.attempt.answer.SingleChoiceAnswer;
import ru.mephi.trainer.models.attempt.answer.UserAnswer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class UserAnswerParser {

    public UserAnswer parse(String answer, TaskType taskType) {
        return switch (taskType) {
            case OPEN_ANSWER, ERROR_FINDING -> parsePlainText(answer, taskType);
            case SINGLE_CHOICE -> parseSingleChoice(answer);
            case MULTIPLE_CHOICE -> parseMultipleChoice(answer);
        };
    }

    private UserAnswer parsePlainText(String answer, TaskType taskType) {
        if (taskType == TaskType.OPEN_ANSWER) {
            return OpenAnswer.builder().answer(answer).build();
        } else if (taskType == TaskType.ERROR_FINDING) {
            return ErrorFindingAnswer.builder().answer(answer).build();
        }
        throw new IllegalArgumentException("Unexpected task type for plain text: " + taskType);
    }

    private SingleChoiceAnswer parseSingleChoice(String answer) {
        try {
            int number = Integer.parseInt(answer);
            return SingleChoiceAnswer.builder()
                    .selectedOrdinal(number)
                    .build();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing ordinal for SINGLE_CHOICE answer:" + e.getMessage());
        }
    }

    private MultipleChoiceAnswer parseMultipleChoice(String answer) {
        Set<Integer> selectedOrdinals = parseToIntegerSet(answer);

        return MultipleChoiceAnswer.builder()
                .selectedOrdinals(selectedOrdinals)
                .build();
    }

    private Set<Integer> parseToIntegerSet(String answer) {
        if (answer == null || answer.isBlank()) {
            return Set.of();
        }

        String cleaned = answer.trim();
        if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }

        if (cleaned.isBlank()) {
            return Set.of();
        }

        return Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
