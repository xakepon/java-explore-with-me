package ru.practicum.common.exception;

import jakarta.persistence.EntityNotFoundException;

public class InvalidStateException extends EntityNotFoundException {
    public InvalidStateException(String message) {
        super(message);
    }
}
