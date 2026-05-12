package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.TrainerEntity;
import ru.mephi.trainer.rest.dto.response.task.user.TaskInfoResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<TaskInfoResponse> findTasksByTrainerId(UUID trainerId) {
        String sql = """
                    SELECT 
                        t.id,
                        t.config->>'question',
                        t.task_type
                    FROM tasks t
                    JOIN tasks_trainers tt ON tt.task_id = t.id
                    WHERE tt.trainer_id = ?1
                """;

        List<Object[]> results = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, trainerId)
                .getResultList();

        return results.stream()
                .map(row -> TaskInfoResponse.builder()
                        .id((UUID) row[0])
                        .title((String) row[1])
                        .type((String) row[2])
                        .build())
                .collect(Collectors.toList());
    }

}
