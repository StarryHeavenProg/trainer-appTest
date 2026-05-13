package ru.mephi.trainer.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReviewRequest {
    @NotNull(message = "Правильность ответа обязательно")
    @Pattern(regexp = "true|false", message = "Допустимы только true или false")
    private String isCorrect;

    public Boolean getIsCorrect() {
        return Boolean.parseBoolean(isCorrect);
    }
}