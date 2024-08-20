package ru.practicum.api.responseDto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ParticipationRequestDto {

    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;
}
