package ru.practicum.mapper;

import ru.practicum.ViewStatDto;
import ru.practicum.api.ViewStat;

public final class ViewStatMapper {
    public static ViewStatDto toViewStatDto(ViewStat stat) {
        return ViewStatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }

}
