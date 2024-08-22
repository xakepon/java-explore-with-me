package ru.practicum.api.adminAPI.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.UpdateEventAdminRequest;
import ru.practicum.api.responseDto.EventFullDto;
import ru.practicum.common.enums.AdminStateAction;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.InvalidStateException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.persistence.models.Event;
import ru.practicum.persistence.models.Location;
import ru.practicum.persistence.repository.CategoryRep;
import ru.practicum.persistence.repository.EventRep;
import ru.practicum.persistence.repository.LocationRep;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.common.constants.Constants.formatter;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {
    private final CategoryRep categoryRepository;
    private final LocationRep locationRepository;
    private final EventRep eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<String> states,
                                                  List<Long> categories, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, int from, int size) {
        validateEventState(states);
        List<Event> events = eventRepository.findAllEventsByAdmin(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from / size, size));

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (eventAdminRequest.getStateAction() != null) {
            Map<AdminStateAction, Runnable> actions = new HashMap<>();

            actions.put(AdminStateAction.PUBLISH_EVENT, () -> {
                validatePublishEvent(event);
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            });

            actions.put(AdminStateAction.REJECT_EVENT, () -> {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ForbiddenException("Cannot reject, the event is already published");
                }
                event.setState(EventState.CANCELED);
            });

            Runnable action = actions.get(eventAdminRequest.getStateAction());
            if (action != null) {
                action.run();
            } else {
                throw new InvalidStateException("Unknown state action: " + eventAdminRequest.getStateAction());
            }
        }

        if (eventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventAdminRequest.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Event cannot start earlier than 2 hours from now");
            }
            event.setEventDate(eventDate);
        }

        updateEventFields(event, eventAdminRequest);
        eventRepository.save(event);
        return EventMapper.toEventFullDto(event);
    }

    private void validateEventState(List<String> states) {
        if (states != null) {
            states.forEach(state -> {
                if (!isValidEventState(state)) {
                    throw new ValidationException("Unknown state: " + state);
                }
            });
        }
    }

    private boolean isValidEventState(String state) {
        try {
            EventState.valueOf(state);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void validatePublishEvent(Event event) {
        if (event.getState() != EventState.PENDING) {
            throw new ForbiddenException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (event.getPublishedOn() != null && event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
            throw new ValidationException("Cannot publish the event earlier than 1 hour");
        }
    }

    private void updateEventFields(Event event, UpdateEventAdminRequest eventDto) {
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (eventDto.getCategory() != null) {
            Long catId = eventDto.getCategory();
            event.setCategory(categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId))));
        }

        if (eventDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(eventDto.getLocation().getLat());
            location.setLon(eventDto.getLocation().getLon());
            event.setLocation(location);
            locationRepository.save(location);
        }
    }

}
