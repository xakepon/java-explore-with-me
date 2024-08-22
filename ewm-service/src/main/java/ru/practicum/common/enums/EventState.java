package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

   /*public static Optional<EventState> from(String stringState) {
        for (EventState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }*/

    public static Optional<EventState> from(String stringState) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringState))
                .findFirst();
    }

}
