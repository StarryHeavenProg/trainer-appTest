package ru.mephi.trainer.rest.dto.response.task.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfoResponse {
    private UUID id;
    private String title;
    private String type;
}
