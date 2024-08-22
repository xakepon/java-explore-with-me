package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SortState {

    EVENT_DATE,
    VIEWS;

    /*public static Optional<SortState> from(String stringRequestSort) {
        for (SortState status : values()) {
            if (status.name().equalsIgnoreCase(stringRequestSort)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }*/

    public static Optional<SortState> from(String stringRequestSort) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringRequestSort))
                .findFirst();
    }

}
