package ru.practicum.api.publicAPI.compilation;

import ru.practicum.api.responseDto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {


    List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);

}
