package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import ru.mephi.trainer.entity.TaskEntity;
import ru.mephi.trainer.entity.enums.AttemptStatus;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.rest.dto.response.task.user.TaskResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TaskEntity, UUID> {

    public Optional<TaskResponse> getTaskWithUserAttempt(UUID userId, UUID trainerId, UUID taskId) {
        String sql = """
                SELECT
                    t.id,
                    t.task_type,
                    t.config->>'question' as question,
                    t.config->>'answerChoices' as answer_choices,
                    COALESCE(CAST(t.config->>'points' AS INTEGER), 0) as points,
                    COALESCE(CAST(t.config->>'mistakeCost' AS INTEGER), 0) as mistake_cost,
                    COALESCE(CAST(t.config->>'maxAttempts' AS INTEGER), 0) as max_attempts,
                    ta.points as user_points,
                    ta.status as attempt_status,
                    t.config->>'context' as context,
                    (SELECT COUNT(ta2.id)
                         FROM task_attempts ta2
                         JOIN tasks_trainers tt2 ON tt2.id = ta2.task_id
                         WHERE tt2.task_id = t.id AND tt2.trainer_id = ?2 AND ta2.user_id = ?1
                    ) as user_attempts_count,
                    ta.user_answer as answer
                FROM tasks t
                JOIN tasks_trainers tt ON tt.task_id = t.id
                LEFT JOIN task_attempts ta ON ta.task_id = tt.id AND ta.user_id = ?1
                WHERE tt.trainer_id = ?2 AND t.id = ?3
                ORDER BY ta.created_at DESC
                LIMIT 1
                """;
        try {
            Object[] result = (Object[]) getEntityManager()
                    .createNativeQuery(sql)
                    .setParameter(1, userId)
                    .setParameter(2, trainerId)
                    .setParameter(3, taskId)
                    .getSingleResult();

            return Optional.of(mapRowToTaskResponse(result));
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<TaskResponse> getTasksWithUserAttempt(UUID userId, UUID trainerId) {
        String sql = """
                    SELECT DISTINCT ON (t.id)
                        t.id,
                        t.task_type,
                        t.config->>'question' as question,
                        t.config->>'answerChoices' as answer_choices,
                        COALESCE(CAST(t.config->>'points' AS INTEGER), 0) as points,
                        COALESCE(CAST(t.config->>'mistakeCost' AS INTEGER), 0) as mistake_cost,
                        COALESCE(CAST(t.config->>'maxAttempts' AS INTEGER), 0) as max_attempts,
                        ta.points as user_points,
                        ta.status as attempt_status,
                        t.config->>'context' as context,
                        (SELECT COUNT(ta2.id)
                         FROM task_attempts ta2
                         JOIN tasks_trainers tt2 ON tt2.id = ta2.task_id
                         WHERE tt2.task_id = t.id AND tt2.trainer_id = ?2 AND ta2.user_id = ?1
                        ) as user_attempts_count,
                        ta.user_answer as user_answer
                    FROM tasks t
                    JOIN tasks_trainers tt ON tt.task_id = t.id
                    LEFT JOIN task_attempts ta ON ta.task_id = tt.id AND ta.user_id = ?1
                    WHERE tt.trainer_id = ?2
                    ORDER BY t.id, ta.created_at DESC
                """;
        List<Object[]> rows = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, userId)
                .setParameter(2, trainerId)
                .getResultList();

        return rows.stream()
                .map(this::mapRowToTaskResponse)
                .toList();
    }

    private TaskResponse mapRowToTaskResponse(Object[] row) {
        TaskResponse dto = new TaskResponse();
        dto.setId((UUID) row[0]);
        dto.setTaskType(TaskType.valueOf((String) row[1]));
        dto.setQuestion((String) row[2]);
        dto.setAnswerChoices((String) row[3]);
        dto.setPoints(((Number) row[4]).intValue());
        dto.setMistakeCost(((Number) row[5]).intValue());
        dto.setMaxAttempts(((Number) row[6]).intValue());
        dto.setContext((String) row[9]);
        dto.setUserAttempts(((Number) row[10]).intValue());
        dto.setAnswer((String) row[11]);

        if (row[7] != null) {
            dto.setUserPoints(((Number) row[7]).intValue());
        }
        if (row[8] != null) {
            dto.setAttemptStatus(AttemptStatus.valueOf((String) row[8]));
        }
        return dto;
    }

    /**
     * Находит задание по ID с предварительной загрузкой связей с тренажерами (TaskTrainer).
     * НЕ делает второй join fetch для загрузки самих тренажеров (Trainer).
     */
    public Optional<TaskEntity> findByIdWithTrainersLinks(UUID id) {
        return find("FROM TaskEntity t LEFT JOIN FETCH t.trainerLinks WHERE t.id = ?1", id)
                .firstResultOptional();
    }
}
