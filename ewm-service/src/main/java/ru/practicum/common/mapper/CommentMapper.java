package ru.practicum.common.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment toNewComment(User user, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .text(newCommentDto.getText())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .created(comment.getCreated().format(formatter))
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .status(comment.getStatus().toString())
                .text(comment.getText())
                .build();
    }

    public List<CommentDto> getCommentsDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    public void adminUpdateCommentFromDto(Comment comment, NewCommentDto newCommentDto) {
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.APPROVED);
    }

    public void privateUpdateCommentFromDto(Comment comment, NewCommentDto newCommentDto) {
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);
    }

}
