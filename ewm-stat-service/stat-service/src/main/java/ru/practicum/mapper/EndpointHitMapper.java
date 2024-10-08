package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.statistic.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.constants.Constants.formatter;


public final class EndpointHitMapper {

    private EndpointHitMapper() {

    }

    public static EndpointHit toEndpointHit(EndpointHitDto hitDto) {
        return EndpointHit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(),formatter))
                .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().toString())
                .build();
    }

}
