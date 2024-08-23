package ru.practicum.api.adminAPI.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.requestDto.NewUserRequest;
import ru.practicum.api.responseDto.UserDto;
import ru.practicum.common.mapper.UserMapper;
import ru.practicum.persistence.repository.UserRep;
import ru.practicum.persistence.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRep repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> userIds, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return userIds != null ? getUsersWithListIds(userIds, page) : getUsersWithoutListIds(page);
    }

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        User newUser = UserMapper.toUser(newUserRequest);
        repository.save(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }


    private List<UserDto> getUsersWithListIds(List<Long> ids, Pageable page) {
        return repository.findAllByIdIn(ids, page).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }


    private List<UserDto> getUsersWithoutListIds(Pageable page) {
        return repository.findAll(page).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}
