package ru.practicum.api.adminAPI.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewCommentDto;
import ru.practicum.api.responseDto.CommentDto;
import ru.practicum.common.enums.CommentStatus;
import ru.practicum.common.exception.InvalidStateException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CommentMapper;
import ru.practicum.persistence.models.Comment;
import ru.practicum.persistence.repository.CommentRep;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentServiceImpl implements AdminCommentService {
    private final CommentRep commentRep;

    @Override
    public CommentDto updateCommentStatusByAdmin(Long comId, String requestStatus) {
        Comment comment = commentRep.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found,", comId)));


        CommentStatus status = Optional.ofNullable(requestStatus)
                .flatMap(CommentStatus::from)
                .orElseThrow(() -> new InvalidStateException(String.format("Unknown status value=%s", requestStatus)));

        comment.setStatus(status);
        comment.setCreated(LocalDateTime.now());
        commentRep.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateEventCommentByAdmin(Long userId, Long comId, NewCommentDto newCommentDto) {
        Comment comment = commentRep.findByIdAndAuthorId(comId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d and authorId=%d was not found,",
                        comId, userId)));
        CommentMapper.adminUpdateCommentFromDto(comment, newCommentDto);
        commentRep.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getEventCommentByAdmin(Long comId) {
        Comment comment = commentRep.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found,", comId)));
        return CommentMapper.toCommentDto(comment);
    }

}
