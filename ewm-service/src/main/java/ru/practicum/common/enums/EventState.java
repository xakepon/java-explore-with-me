package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventState> from(String stringState) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringState))
                .findFirst();
    }

}
