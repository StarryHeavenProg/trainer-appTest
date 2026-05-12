package ru.mephi.trainer.models.taskconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerChoice {
    private Integer ordinal;
    private String choice;
}
