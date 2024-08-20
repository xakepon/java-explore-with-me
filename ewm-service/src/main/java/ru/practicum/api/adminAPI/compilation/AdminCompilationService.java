package ru.practicum.api.adminAPI.compilation;

import ru.practicum.api.requestDto.NewCompilationDto;
import ru.practicum.api.requestDto.UpdateCompilationRequest;
import ru.practicum.api.responseDto.CompilationDto;

public interface AdminCompilationService {

    CompilationDto createCompilationByAdmin(NewCompilationDto compilationDto);

    void deleteCompilationByAdmin(Long compId);

    CompilationDto updateCompilationByAdmin(Long compId, UpdateCompilationRequest compilationRequest);

}
