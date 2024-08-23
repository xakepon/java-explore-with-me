package ru.practicum.common.mapper;

import ru.practicum.api.responseDto.UserDto;
import ru.practicum.api.responseDto.UserShortDto;
import ru.practicum.persistence.models.User;
import ru.practicum.api.requestDto.NewUserRequest;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toUser(NewUserRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
