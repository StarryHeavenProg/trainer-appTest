package ru.mephi.trainer.exception;

public class CurrentUserNotFoundException extends RuntimeException {

    public CurrentUserNotFoundException(String message) {
        super(message);
    }
}
