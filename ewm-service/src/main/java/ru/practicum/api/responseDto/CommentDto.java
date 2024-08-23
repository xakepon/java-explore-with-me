package ru.practicum.api.responseDto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto author;
    private String created;
    private String status;
    private String text;
}
