package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    @Override
    public User createUser(User newUser) {
        validateUserEmail(newUser);
        newUser.setId(currentId);
        users.put(currentId, newUser);
        log.info("Новый пользователь с ID {} добавлен в репозиторий", currentId);
        currentId += 1;
        return newUser;
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        if (!users.containsKey(userId)) {
            String message = "Пользователь с ID " + userId + " для обновления не обнаружен";
            log.error(message);
            throw new NotFoundException(message);
        }
        User oldUser = users.get(userId);
        if (updatedUser.getName() != null) {
            oldUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            validateUserEmail(updatedUser);
            oldUser.setEmail(updatedUser.getEmail());
        }
        log.info("Пользователь с ID {} обновлен в репозиторий", currentId);
        return oldUser;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Выведен список пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            String message = "Пользователь с ID " + userId + " не обнаружен";
            log.error(message);
            throw new NotFoundException(message);
        }
        log.info("Найден пользователь с ID {}", userId);
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        if (!users.containsKey(userId)) {
            String message = "Пользователь с ID " + userId + " для удаления не обнаружен";
            log.error(message);
            throw new NotFoundException(message);
        }
        users.remove(userId);
        log.info("Удален пользователь с ID {}", userId);
    }

    private void validateUserEmail(User newUser) {
        String email = newUser.getEmail();
        if (users.values().stream().map(User::getEmail).toList().contains(email)) {
            String message = "Такой адрес электронной почты уже используется";
            log.error(message);
            throw new ConflictException(message);
        }
    }
}
