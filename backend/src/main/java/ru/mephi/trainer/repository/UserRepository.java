package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).singleResultOptional();
    }

    public boolean existsByEmail(String email) {
        return find("SELECT EXISTS(SELECT 1 FROM UserEntity WHERE email = ?1)", email)
                .project(Boolean.class)
                .firstResultOptional()
                .orElse(false);
    }
}
