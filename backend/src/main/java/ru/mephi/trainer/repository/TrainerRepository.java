package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TrainerEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class TrainerRepository implements PanacheRepositoryBase<TrainerEntity, UUID> {

    public Integer getTotalTasks(UUID trainerId) {
        String sql = """
                    SELECT COUNT(tt.task_id)
                    FROM tasks_trainers tt
                    WHERE tt.trainer_id = CAST(?1 AS UUID)
                """;

        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, trainerId)
                .getSingleResult();

        return (result != null) ? ((Number) result).intValue() : 0;
    }

    public List<TrainerEntity> findByIds(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return list("id in ?1", ids);
    }
}
