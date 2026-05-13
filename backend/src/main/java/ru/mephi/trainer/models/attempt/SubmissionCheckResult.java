package ru.mephi.trainer.models.attempt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.entity.enums.AttemptStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCheckResult {

    private String message;
    private AttemptStatus attemptStatus;
}
