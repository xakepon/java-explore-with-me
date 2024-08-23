package ru.practicum.api.privateAPI.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.responseDto.EventRequestStatusUpdateResult;
import ru.practicum.api.responseDto.ParticipationRequestDto;
import ru.practicum.api.requestDto.EventRequestStatusUpdateRequest;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.ParticipationStatus;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.ParticipationMapper;
import ru.practicum.persistence.models.Event;
import ru.practicum.persistence.models.ParticipationRequest;
import ru.practicum.persistence.models.User;
import ru.practicum.persistence.repository.EventRep;
import ru.practicum.persistence.repository.ParticipationRep;
import ru.practicum.persistence.repository.UserRep;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateParticipationServiceImpl implements PrivateParticipationService {
    private final ParticipationRep repository;
    private final UserRep userRepository;
    private final EventRep eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        validateUserId(userId);
        List<Optional<ParticipationRequest>> requests = repository.findByRequesterId(userId);
        return requests.stream()
                .flatMap(Optional::stream)
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequestByUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found,", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found,", eventId)));

        ParticipationRequest request = repository.findByRequesterIdAndEventId(userId, eventId);
        if (request != null) {
            throw new ForbiddenException("Request already added");
        }
        if (event.getInitiator() == null) {
            throw new ForbiddenException("Event does not have initiator");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Initiator could not add the request to own event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenException("Event is not published yet");
        }
        if (event.getParticipantLimit() != 0
                && repository.countByEventIdAndStatus(eventId, ParticipationStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new ForbiddenException("The participant limit has been reached");
        }
        ParticipationStatus status = (event.isRequestModeration() && event.getParticipantLimit() != 0)
                ? ParticipationStatus.PENDING
                : ParticipationStatus.CONFIRMED;

        ParticipationRequest newRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();

        repository.save(newRequest);
        return ParticipationMapper.toParticipationRequestDto(newRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        validateUserId(userId);
        ParticipationRequest requestToCancel = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found,", requestId)));
        requestToCancel.setStatus(ParticipationStatus.CANCELED);

        repository.save(requestToCancel);
        return ParticipationMapper.toParticipationRequestDto(requestToCancel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsForUserEventByUser(Long userId, Long eventId) {
        validateUserId(userId);
        return repository.findByEventIn(eventRepository.findByIdAndInitiatorId(eventId, userId))
                .stream()
                .flatMap(Optional::stream)
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest eventRequestStatusUpdate) {
        validateUserId(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found,", eventId)));

        List<ParticipationRequest> requests = repository.findAllById(eventRequestStatusUpdate.getRequestIds());
        EventRequestStatusUpdateResult resultDto = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        ParticipationStatus newStatus;
        try {
            newStatus = ParticipationStatus.valueOf(String.valueOf(eventRequestStatusUpdate.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status value: " + eventRequestStatusUpdate.getStatus());
        }

        if (newStatus == ParticipationStatus.CONFIRMED) {
            doConfirmRequests(event, requests, resultDto);
        } else if (newStatus == ParticipationStatus.REJECTED) {
            doRejectRequests(requests, resultDto);
        }
        return resultDto;
    }

    private void doConfirmRequests(Event event, List<ParticipationRequest> requests,
                                   EventRequestStatusUpdateResult resultDto) {
        int limitParticipants = event.getParticipantLimit();
        int countParticipants = repository.countByEventIdAndStatus(event.getId(), ParticipationStatus.CONFIRMED);

        if (limitParticipants == 0 || !event.isRequestModeration()) {
            throw new ForbiddenException("Do not need to accept request, limit=0 : pre-moderation off");
        }
        if (countParticipants > limitParticipants) {
            throw new ForbiddenException("Participant limit is full");
        }

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != ParticipationStatus.PENDING) {
                throw new ForbiddenException("Request must have status PENDING");
            }
        }

        requests.forEach(request -> {
            if (countParticipants < limitParticipants) {
                request.setStatus(ParticipationStatus.CONFIRMED);
                resultDto.getConfirmedRequests().add(ParticipationMapper.toParticipationRequestDto(request));
            } else {
                request.setStatus(ParticipationStatus.REJECTED);
                resultDto.getRejectedRequests().add(ParticipationMapper.toParticipationRequestDto(request));
            }
        });
        repository.saveAll(requests);

        if (countParticipants == limitParticipants) {
            throw new ForbiddenException("Limit is full");
        }
        repository.updateStatusByEventAndCurrentStatus(event, ParticipationStatus.PENDING, ParticipationStatus.REJECTED);
    }

    private void doRejectRequests(List<ParticipationRequest> requests, EventRequestStatusUpdateResult resultDto) {
        requests.stream()
                .filter(request -> {
                    if (request.getStatus() != ParticipationStatus.PENDING) {
                        throw new ForbiddenException("Request must have status PENDING");
                    }
                    return true;
                })
                .forEach(request -> {
                    request.setStatus(ParticipationStatus.REJECTED);
                    resultDto.getRejectedRequests().add(ParticipationMapper.toParticipationRequestDto(request));
                });
        repository.saveAll(requests);
    }

    private void validateUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found,", userId)));
    }

}
