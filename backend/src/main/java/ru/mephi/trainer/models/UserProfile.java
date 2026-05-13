package ru.mephi.trainer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer totalScore;
    private List<TrainerPercentProgress> trainerProgressPercent;
    private Instant createdAt;
}
