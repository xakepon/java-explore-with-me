package ru.practicum.api.requestDto;

import lombok.*;
import ru.practicum.common.enums.RequestStatus;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {


    private List<Long> requestIds;
    private RequestStatus status;
}
