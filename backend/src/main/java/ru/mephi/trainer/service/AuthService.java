package ru.mephi.trainer.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.UserEntity;
import ru.mephi.trainer.exception.EmailAlreadyExistsException;
import ru.mephi.trainer.exception.FailedLoginException;
import ru.mephi.trainer.repository.UserRepository;
import ru.mephi.trainer.rest.dto.request.auth.RegistrationRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static ru.mephi.trainer.util.SecurityUtil.toSecurityRole;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity register(RegistrationRequest request) {
        log.info("Registering new user");

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists");
            throw new EmailAlreadyExistsException("User with this email already exists");
        }

        String passwordHash = BcryptUtil.bcryptHash(request.getPassword());

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userRole(request.getUserRole())
                .passwordHash(passwordHash)
                .build();

        userRepository.persist(user);

        return user;
    }

    public UserEntity authenticate(String email, String password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !BcryptUtil.matches(password, user.get().getPasswordHash())) {
            log.warn("Failed login attempt");
            throw new FailedLoginException("Invalid email or password");
        }
        return user.get();
    }

    public String generateToken(UserEntity user) {
        return Jwt.issuer("trainer-app")
                .upn(user.getEmail())
                .groups(Set.of(toSecurityRole(user.getUserRole())))
                .claim("userId", user.getId())
                .expiresAt(Instant.now().plusSeconds(3600).getEpochSecond()) // 1 час
                .sign();
    }
}
