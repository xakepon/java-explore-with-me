package ru.practicum.api.publicAPI.comment;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.responseDto.CommentDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class PublicCommentController {

    private final PublicCommentService service;

    @GetMapping(path = "/events/{eventId}/comments")
    public List<CommentDto> getAllEventComments(@Validated @Positive @PathVariable Long eventId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get-request: getAllComments, eventId={} , from={} , size={}", eventId, from, size);
        return service.getAllEventComments(eventId, from, size);
    }

}
