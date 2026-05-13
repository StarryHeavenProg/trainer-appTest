package ru.mephi.trainer.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidCommandDataException extends RuntimeException {

    private final Set<String> errors;

    public InvalidCommandDataException(String message) {
        super(message);
        this.errors = message != null ? Set.of(message) : Set.of();
    }

    public InvalidCommandDataException(Set<String> errors) {
        super("Validation failed");
        this.errors = errors != null ? Set.copyOf(errors) : Set.of();
    }
}
