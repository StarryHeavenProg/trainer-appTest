package ru.mephi.trainer.exception;

public class UserUseMaxAttemptsLimitException extends RuntimeException {
    public UserUseMaxAttemptsLimitException(String message) {
        super(message);
    }
}
