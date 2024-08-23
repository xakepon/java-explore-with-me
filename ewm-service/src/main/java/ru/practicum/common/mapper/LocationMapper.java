package ru.practicum.common.mapper;

import ru.practicum.persistence.models.Location;
import ru.practicum.api.responseDto.LocationDto;

public final class LocationMapper {

    private LocationMapper() {
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

}
