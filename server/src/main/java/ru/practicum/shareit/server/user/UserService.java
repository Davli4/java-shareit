package ru.practicum.shareit.server.user;

import ru.practicum.shareit.server.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto getById(Long id);

    List<UserDto> getAll();

    void delete(Long id);
}