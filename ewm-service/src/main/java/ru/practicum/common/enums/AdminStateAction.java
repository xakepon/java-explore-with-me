package ru.practicum.common.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AdminStateAction {

    PUBLISH_EVENT,
    REJECT_EVENT;

    /*public static Optional<AdminStateAction> from(String stringStateAction) {
        for (AdminStateAction state : values()) {
            if (state.name().equalsIgnoreCase(stringStateAction)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }*/

    public static Optional<AdminStateAction> from(String stringStateAction) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(stringStateAction))
                .findFirst();
    }
}
