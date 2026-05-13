package ru.mephi.trainer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.mephi.trainer.entity.enums.TaskType;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "trainerLinks")
@Table(name = "tasks", schema = "public")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "task_type", columnDefinition = "task_types", nullable = false)
    private TaskType taskType;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String config;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskTrainerEntity> trainerLinks = new HashSet<>();

    public void addTrainerLink(TaskTrainerEntity link) {
        trainerLinks.add(link);
        link.setTask(this);
    }

    public void removeTrainerLink(TaskTrainerEntity link) {
        trainerLinks.remove(link);
        link.setTask(null);
    }

    public void clearTrainerLinks() {
        trainerLinks.forEach(link -> link.setTask(null));
        trainerLinks.clear();
    }
}
