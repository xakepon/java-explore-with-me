package ru.practicum.api.requestDto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.api.responseDto.LocationDto;
import ru.practicum.common.enums.PrivateStateAction;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateEventUserRequest {


    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;
    private LocationDto location;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
    private PrivateStateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
