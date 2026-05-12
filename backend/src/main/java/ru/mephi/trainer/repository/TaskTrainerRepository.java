package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TaskTrainerEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskTrainerRepository implements PanacheRepositoryBase<TaskTrainerEntity, UUID> {

    public long deleteAllByTaskId(UUID taskId){
        return delete("DELETE FROM TaskTrainerEntity WHERE task.id = ?1", taskId);
    }

    public long deleteAllByTrainerId(UUID trainerId){
        return delete("DELETE FROM TaskTrainerEntity WHERE trainer.id = ?1", trainerId);
    }

    public Optional<TaskTrainerEntity> findByTrainerIdAndTaskId(UUID trainerId, UUID taskId) {
        return find("trainer.id = ?1 and task.id = ?2", trainerId, taskId)
                .firstResultOptional();
    }
}
