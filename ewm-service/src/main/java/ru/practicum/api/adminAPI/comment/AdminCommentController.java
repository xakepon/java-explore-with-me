package ru.practicum.api.adminAPI.comment;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.requestDto.NewCommentDto;
import ru.practicum.api.responseDto.CommentDto;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AdminCommentController {

    private final AdminCommentService service;

    @GetMapping(path = "/admin/comments/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getEventCommentByAdmin(@Validated @Positive @PathVariable Long comId) {
        log.info("Get-request: getEventCommentByAdmin comId={}", comId);
        return service.getEventCommentByAdmin(comId);
    }

    @PatchMapping(path = "/admin/users/{userId}/comments/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateEventCommentByAdmin(@Validated @Positive @PathVariable Long userId,
                                                @Validated @Positive @PathVariable Long comId,
                                                @Validated @RequestBody NewCommentDto newCommentDto) {
        log.info("Patch-request: updateCommentByUser comId={}, newCommentDto={}", comId, newCommentDto);
        return service.updateEventCommentByAdmin(userId, comId, newCommentDto);
    }

    @PatchMapping(path = "/admin/comments/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentStatusByAdmin(@Validated @Positive @PathVariable Long comId,
                                                 @RequestParam(defaultValue = "PENDING") String status) {
        log.info("Patch-request: updateEventCommentStatusByAdmin comId={}, status={}", comId, status);
        return service.updateCommentStatusByAdmin(comId, status);
    }

}
