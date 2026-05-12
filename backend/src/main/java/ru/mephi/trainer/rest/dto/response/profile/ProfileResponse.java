package ru.mephi.trainer.rest.dto.response.profile;

import lombok.Data;

import java.util.List;

@Data
public class ProfileResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer totalScore;
    private List<TrainerProgressPercentResponse> trainerProgressPercent;
    private String createdAt;
}

