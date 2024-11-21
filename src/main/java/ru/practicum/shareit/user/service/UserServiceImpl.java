package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User newUser) {
        validateUser(newUser);
        log.info("Вызван сервисный метод сохранения пользователя");
        return userRepository.createUser(newUser);
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        if (updatedUser.getEmail() != null) {
            if (updatedUser.getEmail().isBlank() || !updatedUser.getEmail().contains("@")) {
                String message = "Электронная почта не может быть пустой и должна содержать символ @";
                log.error(message);
                throw new ValidationException(message);
            }
        }
        if (updatedUser.getName() != null) {
            if (updatedUser.getName().isBlank()) {
                String message = "Логин не может быть пустым";
                log.error(message);
                throw new ValidationException(message);
            }
        }
        log.info("Вызван сервисный метод обновления пользователя");
        return userRepository.updateUser(userId, updatedUser);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Вызван сервисный метод вывода списка пользователей");
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(long userId) {
        log.info("Вызван сервисный метод вывода пользователя с ID {}", userId);
        return userRepository.getUserById(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        log.info("Вызван сервисный метод удаления пользователя с ID {}", userId);
        userRepository.deleteUserById(userId);
    }

    private void validateUser(User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            String message = "Электронная почта не может быть пустой или отсутствовать и должна содержать символ @";
            log.error(message);
            throw new ValidationException(message);
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            String message = "Логин не может быть пустым или отсутствовать";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
