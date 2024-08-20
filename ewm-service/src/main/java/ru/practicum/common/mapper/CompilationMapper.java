package ru.practicum.common.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.api.requestDto.NewCompilationDto;
import ru.practicum.api.responseDto.CompilationDto;
import ru.practicum.api.responseDto.EventShortDto;
import ru.practicum.persistence.models.Compilation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import ru.practicum.persistence.models.Event;

public final class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(getEventShortDtoList(compilation.getEvents()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    private static List<EventShortDto> getEventShortDtoList(List<Event> events) {
        return events != null ? events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()) : Collections.emptyList();

    }
}
