package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestStatus {

    CONFIRMED,
    REJECTED;

    /*public static Optional<RequestStatus> from(String stringRequestStatus) {
        for (RequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringRequestStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }*/

    public static Optional<RequestStatus> from(String stringRequestStatus) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringRequestStatus))
                .findFirst();
    }
}
