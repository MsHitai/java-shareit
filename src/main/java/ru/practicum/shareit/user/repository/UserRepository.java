package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    User findById(long id);

    User partialUpdateUser(User user, User userToPatch);

    void deleteUser(User user);
}
