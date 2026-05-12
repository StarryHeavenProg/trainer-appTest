package ru.mephi.trainer.models.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.trainer.models.taskconfig.TaskConfig;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskCommand {
    private TaskConfig config;
    private Set<UUID> trainerIds;
}
