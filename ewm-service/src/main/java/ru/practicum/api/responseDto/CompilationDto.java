package ru.practicum.api.responseDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    public List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
