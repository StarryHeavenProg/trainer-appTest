package ru.mephi.trainer.rest.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedTaskTrainerPointResponse {
    private UUID id;
    private String name;
    private Integer point;
    private Integer maxPoint;
}