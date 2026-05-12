package ru.mephi.trainer.exception;

public class LastTaskAttemptStatusNotFailedException extends RuntimeException {
    public LastTaskAttemptStatusNotFailedException(String message) {
        super(message);
    }
}
