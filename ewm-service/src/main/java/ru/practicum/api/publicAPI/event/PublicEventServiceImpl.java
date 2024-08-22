package ru.practicum.api.publicAPI.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.api.responseDto.EventFullDto;
import ru.practicum.api.responseDto.EventShortDto;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.SortState;
import ru.practicum.common.exception.InvalidStateException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.mapper.ParticipationMapper;
import ru.practicum.persistence.models.Event;
import ru.practicum.persistence.repository.EventRep;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.common.constants.Constants.formatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private static final int PLUS_ONE_VIEW = 1;
    private final EventRep repository;

    @Override
    public List<EventShortDto> getAllPublicEvents(String text, List<Long> categories, Boolean paid,
                                                  String rangeStart, String rangeEnd,
                                                  boolean onlyAvailable, String sort, int from, int size) {
        LocalDateTime startTime = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now();
        LocalDateTime endTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;
        validateDateRange(startTime, endTime);

        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = repository.findAllPublicEvents(text, categories, paid, startTime, endTime, page);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> ParticipationMapper.confirmedRequestsCounts(e.getRequests()) < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        List<EventShortDto> shortEvents = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        if (sort != null) {
            sortEvents(shortEvents, sort);
        }
        return shortEvents.isEmpty() ? Collections.emptyList() : shortEvents;
    }

    @Override
    public EventFullDto getPublicEvent(Long eventId) {
        Event event = repository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found,", eventId)));
        event.setViews(event.getViews() + PLUS_ONE_VIEW);
        return EventMapper.toEventFullDto(event);
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("RangeStart isAfter rangeEnd");
        }
    }

    private void sortEvents(List<EventShortDto> events, String sort) {
        Map<SortState, Runnable> sortActions = new HashMap<>();
        sortActions.put(SortState.EVENT_DATE, () -> events.sort(Comparator.comparing(EventShortDto::getEventDate)));
        sortActions.put(SortState.VIEWS, () -> events.sort(Comparator.comparing(EventShortDto::getViews)));

        Optional<SortState> sortState = SortState.from(sort);
        /*if (sortState.isPresent()) {
            Runnable action = sortActions.get(sortState.get());
            if (action != null) {
                action.run();
            } else {
                throw new InvalidStateException(String.format("Undefined sort value=%s", sort));
            }
        } else {
            throw new InvalidStateException(String.format("Undefined sort value=%s", sort));
        }*/
        sortState.ifPresentOrElse(
                state -> {
                    Runnable action = sortActions.get(state);
                    if (action != null) {
                        action.run();
                    } else {
                        throw new InvalidStateException(String.format("Undefined sort value=%s", sort));
                    }
                },
                () -> {
                    throw new InvalidStateException(String.format("Undefined sort value=%s", sort));
                }
        );
    }

}
