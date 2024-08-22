package ru.practicum.common.mapper;

import ru.practicum.api.responseDto.ParticipationRequestDto;
import ru.practicum.common.enums.ParticipationStatus;
import ru.practicum.persistence.models.ParticipationRequest;


import java.util.List;

import static ru.practicum.common.constants.Constants.formatter;

public final class ParticipationMapper {

    private static final int EMPTY_REQUESTS_COUNT = 0;

    private ParticipationMapper() {
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated().format(formatter))
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }

    public static long confirmedRequestsCounts(List<ParticipationRequest> requests) {
       /* if (requests == null) {
            return EMPTY_REQUESTS_COUNT;
        }
        return requests.stream()
                .filter(request -> request.getStatus() == ParticipationStatus.CONFIRMED)
                .count();*/
        return requests == null? EMPTY_REQUESTS_COUNT : requests.stream()
                .filter(request -> request.getStatus() == ParticipationStatus.CONFIRMED)
                .count();

    }
}
