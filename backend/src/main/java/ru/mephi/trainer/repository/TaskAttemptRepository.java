package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ru.mephi.trainer.entity.TaskAttemptEntity;
import ru.mephi.trainer.rest.dto.response.task.expert.AnswerTaskResponse;
import ru.mephi.trainer.rest.dto.response.task.expert.ReviewTaskResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskAttemptRepository implements PanacheRepositoryBase<TaskAttemptEntity, UUID> {

    @PersistenceContext
    private EntityManager em;

    public List<ReviewTaskResponse> getReviewTasksWithDetails() {
        String sql = """
                    SELECT
                        ta.id as attempt_id,
                        t.config->>'question' as task_name,
                        tr.name as trainer_name,
                        u.first_name || ' ' || u.last_name as student_name,
                        u.email as student_email,
                        ta.created_at
                    FROM task_attempts ta
                    JOIN tasks_trainers tt ON tt.id = ta.task_id
                    JOIN tasks t ON t.id = tt.task_id
                    JOIN trainers tr ON tr.id = tt.trainer_id
                    JOIN users u ON u.id = ta.user_id
                    WHERE ta.status = 'REVIEW'
                    ORDER BY ta.created_at DESC
                """;

        List<Object[]> results = getEntityManager()
                .createNativeQuery(sql)
                .getResultList();

        return results.stream()
                .map(row -> ReviewTaskResponse.builder()
                        .attemptId((UUID) row[0])
                        .taskName((String) row[1])
                        .trainerName((String) row[2])
                        .studentName((String) row[3])
                        .studentEmail((String) row[4])
                        .createdAt((OffsetDateTime) row[5])
                        .build())
                .toList();
    }

    public Optional<AnswerTaskResponse> getAnswerTask(UUID id) {
        String sql = """
                    SELECT
                        ta.id as attempt_id,
                        t.config->>'question' as task_name,
                        tr.name as trainer_name,
                        u.first_name || ' ' || u.last_name as student_name,
                        u.email as student_email,
                        ta.user_answer as answer,
                        ta.points as points,
                        CAST(t.config->>'points' AS INTEGER) as max_points,
                        ta.created_at
                    FROM task_attempts ta
                    JOIN tasks_trainers tt ON tt.id = ta.task_id
                    JOIN tasks t ON t.id = tt.task_id
                    JOIN trainers tr ON tr.id = tt.trainer_id
                    JOIN users u ON u.id = ta.user_id
                    WHERE ta.id = ?1
                """;
        try {
            Object[] result = (Object[]) em.createNativeQuery(sql)
                    .setParameter(1, id)
                    .getSingleResult();

            return Optional.of(AnswerTaskResponse.builder()
                    .attemptId((UUID) result[0])
                    .taskName((String) result[1])
                    .trainerName((String) result[2])
                    .studentName((String) result[3])
                    .studentEmail((String) result[4])
                    .answer((String) result[5])
                    .points((Integer) result[6])
                    .maxPoints((Integer) result[7])
                    .createdAt((OffsetDateTime) result[8])
                    .build());
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Integer getMaxPointsForAttempt(UUID id) {
        String sql = """
                    SELECT CAST(t.config->>'points' AS INTEGER)
                    FROM task_attempts ta
                    JOIN tasks_trainers tt ON tt.id = ta.task_id
                    JOIN tasks t ON t.id = tt.task_id
                    WHERE ta.id = ?1
                """;

        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, id)
                .getSingleResult();

        return ((Number) result).intValue();
    }

    public Integer getMistakeCost(UUID id) {
        String sql = """
                    SELECT CAST(t.config->>'mistakeCost' AS INTEGER)
                    FROM task_attempts ta
                    JOIN tasks_trainers tt ON tt.id = ta.task_id
                    JOIN tasks t ON t.id = tt.task_id
                    WHERE ta.id = ?1
                """;

        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, id)
                .getSingleResult();

        return ((Number) result).intValue();
    }

    public Integer getAttemptsCount(UUID taskId, UUID userId) {
        String sql = """
                    SELECT COUNT(ta.id)
                    FROM task_attempts ta
                    WHERE ta.task_id = ?1 AND ta.user_id = ?2
                """;

        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, taskId)
                .setParameter(2, userId)
                .getSingleResult();

        return ((Number) result).intValue();
    }

    public Integer getAttemptsCountByTaskAndTrainer(UUID taskId, UUID trainerId, UUID userId) {
        String sql = """
                    SELECT COUNT(ta.id)
                    FROM task_attempts ta
                    JOIN tasks_trainers tt ON tt.id = ta.task_id
                    WHERE tt.task_id = ?1 AND ta.user_id = ?2 AND tt.trainer_id = ?3
                """;

        Object result = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, taskId)
                .setParameter(2, userId)
                .setParameter(3, trainerId)
                .getSingleResult();

        return ((Number) result).intValue();
    }

    public Optional<TaskAttemptEntity> findLastAttempt(UUID userId, UUID taskTrainerId) {
        return find("user.id = ?1 and task.id = ?2 order by createdAt desc", userId, taskTrainerId)
                .firstResultOptional();
    }

}
