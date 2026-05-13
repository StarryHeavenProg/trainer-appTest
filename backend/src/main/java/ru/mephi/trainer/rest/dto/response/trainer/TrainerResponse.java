package ru.mephi.trainer.rest.dto.response.trainer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private UUID id;
    private String name;
    private Instant createdAt;
    private UUID createdBy;
}