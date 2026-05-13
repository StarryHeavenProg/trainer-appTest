package ru.mephi.trainer.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ru.mephi.trainer.rest.dto.response.profile.CompletedTaskTrainerPointResponse;
import ru.mephi.trainer.rest.dto.response.profile.TrainerProgressResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TrainerProgressRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<TrainerProgressResponse> getTrainerProgress(UUID userId, UUID trainerId) {
        String sql = """
                    SELECT
                        t.id as trainer_id,
                        t.name as trainer_name,
                        COALESCE(SUM(ta.points), 0) as earned_score,
                        COALESCE(SUM(CAST(task.config->>'points' AS INTEGER)), 0) as max_possible_score,
                        COUNT(DISTINCT ta.id) as tasks_completed,
                        COUNT(DISTINCT task.id) as total_tasks
                    FROM trainers t
                    LEFT JOIN tasks_trainers tt ON tt.trainer_id = t.id
                    LEFT JOIN tasks task ON task.id = tt.task_id
                    LEFT JOIN task_attempts ta ON ta.task_id = tt.id AND ta.user_id = ?1 AND ta.status = 'COMPLETED'
                    WHERE t.id = ?2
                    GROUP BY t.id, t.name
                """;

        try {
            Object[] result = (Object[]) em.createNativeQuery(sql)
                    .setParameter(1, userId)
                    .setParameter(2, trainerId)
                    .getSingleResult();

            return Optional.of(TrainerProgressResponse.builder()
                    .trainerId((UUID) result[0])
                    .trainerName((String) result[1])
                    .earnedScore(((Number) result[2]).intValue())
                    .maxPossibleScore(((Number) result[3]).intValue())
                    .tasksCompleted(((Number) result[4]).intValue())
                    .totalTasks(((Number) result[5]).intValue())
                    .build());

        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }


    public List<CompletedTaskTrainerPointResponse> getCompletedTaskTrainer(UUID userId, UUID trainerId) {
        String sql = """
                    SELECT
                        t.id,
                        t.config->>'question' as name,
                        COALESCE(ta.points, 0) as point,
                        CAST(t.config->>'points' AS INTEGER) as max_point
                    FROM tasks t
                    JOIN tasks_trainers tt ON tt.task_id = t.id
                    LEFT JOIN task_attempts ta ON ta.task_id = tt.id AND ta.user_id = ?1 AND ta.status = 'COMPLETED'
                    WHERE tt.trainer_id = ?2
                    ORDER BY t.created_at
                """;

        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter(1, userId)
                .setParameter(2, trainerId)
                .getResultList();

        return rows.stream()
                .map(row -> {
                    CompletedTaskTrainerPointResponse dto = new CompletedTaskTrainerPointResponse();
                    dto.setId((UUID) row[0]);
                    dto.setName((String) row[1]);
                    dto.setPoint(((Number) row[2]).intValue());
                    dto.setMaxPoint(((Number) row[3]).intValue());
                    return dto;
                })
                .toList();
    }
}