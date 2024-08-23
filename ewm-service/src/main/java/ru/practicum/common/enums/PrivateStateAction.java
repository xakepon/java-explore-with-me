package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PrivateStateAction {

    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<PrivateStateAction> from(String stringStateAction) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringStateAction))
                .findFirst();
    }

}
