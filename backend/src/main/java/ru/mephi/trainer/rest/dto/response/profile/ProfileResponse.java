package ru.mephi.trainer.rest.dto.response.profile;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class ProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer totalScore;
    private List<TrainerProgressPercentResponse> trainerProgressPercent;
    private Instant createdAt;
}
