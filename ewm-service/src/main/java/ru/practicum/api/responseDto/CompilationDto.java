package ru.practicum.api.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    public List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
