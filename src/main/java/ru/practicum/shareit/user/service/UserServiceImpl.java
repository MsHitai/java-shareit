package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        return repository.save(user);
    }

    @Override
    public User findById(long id) {
        return repository.findById(id);
    }

    @Override
    public User partialUpdateUser(UserDto userDto, long id) {
        User userToPatch = findById(id);
        User user = UserMapper.mapToUser(userDto);
        if (userToPatch.getEmail().equals(user.getEmail())) {
            return userToPatch;
        }
        return repository.partialUpdateUser(user, userToPatch);
    }

    @Override
    public void deleteUser(long id) {
        User user = findById(id);
        repository.deleteUser(user);
    }
}
