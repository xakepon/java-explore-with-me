package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestStatus {

    CONFIRMED,
    REJECTED;

    public static Optional<RequestStatus> from(String stringRequestStatus) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringRequestStatus))
                .findFirst();
    }
}
