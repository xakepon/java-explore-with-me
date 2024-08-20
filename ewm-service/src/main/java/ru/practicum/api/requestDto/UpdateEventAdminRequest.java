package ru.practicum.api.requestDto;

import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.api.responseDto.LocationDto;
import ru.practicum.common.enums.AdminStateAction;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;

}
