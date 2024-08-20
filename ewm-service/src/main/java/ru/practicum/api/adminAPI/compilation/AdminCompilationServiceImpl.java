package ru.practicum.api.adminAPI.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewCompilationDto;
import ru.practicum.api.responseDto.CompilationDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.ValidationException;
import ru.practicum.common.mapper.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.repository.CompilationRep;
import ru.practicum.repository.EventRep;
import ru.practicum.api.requestDto.UpdateCompilationRequest;
import ru.practicum.models.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRep compilationRepository;
    private final EventRep eventRepository;

    @Override
    public CompilationDto createCompilationByAdmin(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        if (compilationDto.getEvents() != null) {
            setCompilationEvents(compilation, compilationDto.getEvents());
        }

        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilationByAdmin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilationByAdmin(Long compId, UpdateCompilationRequest compilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        if (compilationRequest.getTitle() != null) {
            String title = compilationRequest.getTitle();
            if (title.isEmpty() || title.length() > 50) {
                throw new ValidationException("CompilationRequest title=%d must be min=1, max=50 characters");
            }
            compilation.setTitle(title);
        }
        if (compilationRequest.getPinned() != null) {
            compilation.setPinned(compilationRequest.getPinned());
        }
        if (compilationRequest.getEvents() != null) {
            setCompilationEvents(compilation, compilationRequest.getEvents());
        }

        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    private void setCompilationEvents(Compilation compilation, List<Long> eventIds) {
        List<Event> events = eventRepository.findByIdIn(eventIds);
        compilation.setEvents(events);
    }

}
