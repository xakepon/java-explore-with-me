package ru.practicum.api.privateAPI.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewEventDto;
import ru.practicum.api.requestDto.UpdateEventUserRequest;
import ru.practicum.api.responseDto.EventFullDto;
import ru.practicum.api.responseDto.EventShortDto;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.PrivateStateAction;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.EventMapper;
import ru.practicum.common.mapper.LocationMapper;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.Location;
import ru.practicum.models.User;
import ru.practicum.repository.CategoryRep;
import ru.practicum.repository.EventRep;
import ru.practicum.repository.LocationRep;
import ru.practicum.repository.UserRep;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventServiceImpl implements PrivateEventService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRep eventRepository;
    private final UserRep userRepository;
    private final LocationRep locationRepository;
    private final CategoryRep categoryRepository;

    @Override
    public List<EventShortDto> getUserEventsByUser(Long userId, int from, int size) {
        validateUser(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size));
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEventByUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found,", userId)));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found,", newEventDto.getCategory())));
        if (newEventDto.getEventDate() != null) {
            LocalDateTime dateTime = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
            validateDate(dateTime);
        }

        Location location = LocationMapper.toLocation(newEventDto.getLocation());
        locationRepository.save(location);

        Event newEvent = EventMapper.toEvent(newEventDto);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(user);
        newEvent.setState(EventState.PENDING);
        newEvent.setLocation(location);
        newEvent.setCategory(category);
        newEvent.setViews(0L);

        eventRepository.save(newEvent);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        validateUser(userId);
        return EventMapper.toEventFullDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId))));
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        validateUser(userId);

        if (eventDto.getEventDate() != null) {
            LocalDateTime dateTime = LocalDateTime.parse(eventDto.getEventDate(), formatter);
            validateDate(dateTime);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        validateEventState(event);

        updateEventFields(event, eventDto);
        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    private void updateEventFields(Event event, UpdateEventUserRequest eventDto) {
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(eventDto.getTitle()).ifPresent(event::setTitle);

        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", eventDto.getCategory())));
            event.setCategory(category);
        }

        if (eventDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(eventDto.getLocation().getLat());
            location.setLon(eventDto.getLocation().getLon());
            event.setLocation(locationRepository.save(location));
        }

        if (eventDto.getStateAction() != null) {
            Map<PrivateStateAction, Runnable> actions = new HashMap<>();

            actions.put(PrivateStateAction.CANCEL_REVIEW, () -> event.setState(EventState.CANCELED));
            actions.put(PrivateStateAction.SEND_TO_REVIEW, () -> event.setState(EventState.PENDING));

            Runnable action = actions.get(eventDto.getStateAction());
            if (action != null) {
                action.run();
            }
        }
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private void validateDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Cannot start earlier than 2 hours from now");
        }
    }

    private void validateEventState(Event existingEvent) {
        if (!EnumSet.of(EventState.PENDING, EventState.CANCELED).contains(existingEvent.getState())) {
            throw new ForbiddenException("Request must have status PENDING or CANCELED");
        }
    }

}
