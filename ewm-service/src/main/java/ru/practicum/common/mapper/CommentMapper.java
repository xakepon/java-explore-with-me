package ru.practicum.common.mapper;

import ru.practicum.api.requestDto.NewCommentDto;
import ru.practicum.api.responseDto.CommentDto;
import ru.practicum.common.enums.CommentStatus;
import ru.practicum.persistence.models.Comment;
import ru.practicum.persistence.models.Event;
import ru.practicum.persistence.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.common.constants.Constants.formatter;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toNewComment(User user, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .text(newCommentDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .created(comment.getCreated().format(formatter))
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .status(comment.getStatus().toString())
                .text(comment.getText())
                .build();
    }

    public static List<CommentDto> getCommentsDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static void adminUpdateCommentFromDto(Comment comment, NewCommentDto newCommentDto) {
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.APPROVED);
    }

    public static void privateUpdateCommentFromDto(Comment comment, NewCommentDto newCommentDto) {
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);
    }
}
