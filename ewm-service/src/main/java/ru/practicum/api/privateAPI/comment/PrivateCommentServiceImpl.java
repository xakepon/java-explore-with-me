package ru.practicum.api.privateAPI.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewCommentDto;
import ru.practicum.api.responseDto.CommentDto;
import ru.practicum.common.exception.ForbiddenException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CommentMapper;
import ru.practicum.persistence.models.Comment;
import ru.practicum.persistence.models.Event;
import ru.practicum.persistence.models.User;
import ru.practicum.persistence.repository.CommentRep;
import ru.practicum.persistence.repository.EventRep;
import ru.practicum.persistence.repository.UserRep;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRep commentRep;
    private final UserRep userRepository;
    private final EventRep eventRepository;

    @Override
    public CommentDto createEventCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found,", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found,", eventId)));

        Comment comment = CommentMapper.toNewComment(user, event, newCommentDto);
        commentRep.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateEventCommentByUser(Long userId, Long comId, NewCommentDto newCommentDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found,", userId)));
        Comment comment = commentRep.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found,", comId)));

        if (comment.getAuthor() == null) {
            throw new NotFoundException("Comment does not have author");
        }
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Only author and admin can update comment");
        }

        CommentMapper.privateUpdateCommentFromDto(comment, newCommentDto);
        commentRep.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteEventCommentByUser(Long userId, Long comId) {
        Comment comment = commentRep.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found,", comId)));
        if (comment.getAuthor() == null) {
            log.error("metod deleteEventCommentByUser NotFoundException");
            throw new NotFoundException("Comment does not have author");
        }
        if (comment.getAuthor().getId().equals(userId)) {
            log.error("metod deleteEventCommentByUser ForbiddenException");
           throw new ForbiddenException("Only author can delete comment");
        }
        commentRep.deleteById(comId);
    }

}
