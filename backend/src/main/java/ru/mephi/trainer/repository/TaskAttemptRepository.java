package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TaskAttemptEntity;

import java.util.UUID;

@ApplicationScoped
public class TaskAttemptRepository implements PanacheRepositoryBase<TaskAttemptEntity, UUID> {
}
