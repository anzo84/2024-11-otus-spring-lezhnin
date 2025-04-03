package ru.otus.hw11.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Object... formatArgs) {
        super(message.formatted(formatArgs));
    }
}
