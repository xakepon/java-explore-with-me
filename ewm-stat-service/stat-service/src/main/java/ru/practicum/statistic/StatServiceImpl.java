package ru.practicum.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.statistic.model.EndpointHit;
import ru.practicum.statistic.model.EndpointHitMapper;
import ru.practicum.statistic.model.ViewStat;
import ru.practicum.statistic.model.ViewStatMapper;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository repository;

    @Transactional
    @Override
    public EndpointHitDto addHit(EndpointHitDto hitDto) {
        EndpointHit hit = repository.save(EndpointHitMapper.toEndpointHit(hitDto));
        return EndpointHitMapper.toEndpointHitDto(hit);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public List<ViewStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Error! Bad params, start is after end!");
        }
        if (unique) {
            return getAllUniqueViewStatList(start, end, uris, Pageable.ofSize(10));
        }
        return getAllViewStatList(start, end, uris, Pageable.ofSize(10));
    }

    private List<ViewStatDto> getAllViewStatList(LocalDateTime start, LocalDateTime end, List<String> uris, Pageable page) {
        return getViewStatDtoList(repository.findAllViewStatList(start, end, uris, page));
    }

    private List<ViewStatDto> getAllUniqueViewStatList(LocalDateTime start, LocalDateTime end, List<String> uris, Pageable page) {
        return getViewStatDtoList(repository.findAllUniqueViewStatList(start, end, uris, page));
    }

    private List<ViewStatDto> getViewStatDtoList(List<ViewStat> viewStats) {
        return viewStats.stream().map(ViewStatMapper::toViewStatDto).collect(Collectors.toList());
    }

}
