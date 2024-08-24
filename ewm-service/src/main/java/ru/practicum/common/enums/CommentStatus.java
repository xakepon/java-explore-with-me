package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum CommentStatus {

    PENDING,
    APPROVED,
    REJECTED,
    SPAM,
    BLOCKED;

    public static Optional<CommentStatus> from(String stringStatus) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringStatus))
                .findFirst();
    }
}
