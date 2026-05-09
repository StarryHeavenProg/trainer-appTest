package ru.mephi.trainer.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;
import ru.mephi.trainer.exception.CurrentUserNotFoundException;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class CurrentUserService {

    private final JsonWebToken jwt;

    public Optional<UUID> getCurrentUserId() {
        String userId = jwt.getClaim("userId");
        if (userId == null) {
            return Optional.empty();
        }
        return Optional.of(UUID.fromString(userId));
    }

    public UUID getCurrentUserIdOrThrow() {
        String userId = jwt.getClaim("userId");
        if (userId == null) {
            throw new CurrentUserNotFoundException("Не удалось определить текущего пользователя");
        }
        return UUID.fromString(userId);
    }
}
