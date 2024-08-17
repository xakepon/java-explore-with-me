package ru.practicum.statistic.model;

import ru.practicum.ViewStatDto;

public final class ViewStatMapper {
    public static ViewStatDto toViewStatDto(ViewStat stat) {
        return ViewStatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }

}
