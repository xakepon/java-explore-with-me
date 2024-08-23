package ru.practicum.api.publicAPI.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.api.responseDto.CompilationDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.mapper.CompilationMapper;
import ru.practicum.persistence.models.Compilation;
import ru.practicum.persistence.repository.CompilationRep;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
        private final CompilationRep compilationRepository;

        @Override
        public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
            Pageable pageable = PageRequest.of(from / size, size);
            List<Compilation> compilations = (pinned != null)
                    ? compilationRepository.findByPinned(pinned, pageable)
                    : compilationRepository.findAll(pageable).getContent();

            List<CompilationDto> compilationDtoList = compilations.stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
            return compilationDtoList.isEmpty() ? Collections.emptyList() : compilationDtoList;
        }

        @Override
        public CompilationDto getCompilationById(Long compId) {
            return CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                    .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found,", compId))));
        }

    }