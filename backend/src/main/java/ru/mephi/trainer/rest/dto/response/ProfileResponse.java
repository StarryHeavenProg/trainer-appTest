package ru.mephi.trainer.rest.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ProfileResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer totalScore; // общее количество баллов
    private List<TrainerProgressPercentResponse> trainerProgressPercent; // процент пройденных тренажеров
    private String createdAt;
}

