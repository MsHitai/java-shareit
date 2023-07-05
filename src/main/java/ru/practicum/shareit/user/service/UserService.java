package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User saveUser(UserDto userDto);

    User findById(long id);

    User partialUpdateUser(UserDto userDto, long id);

    void deleteUser(long id);
}
