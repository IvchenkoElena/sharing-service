package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item createItem(long ownerId, Item item) {
        User owner = userRepository.getUserById(ownerId);
        validateItem(item);
        return itemRepository.createItem(owner, item);
    }

    @Override
    public Item updateItem(long ownerId, long itemId, Item updatedItem) {
        if (updatedItem.getName() != null) {
            if (updatedItem.getName().isBlank()) {
                String message = "Название не может быть пустым или отсутствовать";
                log.error(message);
                throw new ValidationException(message);
            }
        }
        if (updatedItem.getDescription() != null) {
            if (updatedItem.getDescription().isBlank()) {
                String message = "Описание не может быть пустым или отсутствовать";
                log.error(message);
                throw new ValidationException(message);
            }
        }
        User owner = userRepository.getUserById(ownerId);
        return itemRepository.updateItem(owner, itemId, updatedItem);
    }

    @Override
    public List<Item> getAllItems(long ownerId) {
        return itemRepository.getAllItemsByOwnerId(ownerId);
    }

    @Override
    public Item getItemById(long ownerId, long itemId) {
        return itemRepository.getItemById(ownerId, itemId);
    }

    @Override
    public List<Item> searchItems(Optional<Long> ownerId, String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItems(ownerId, text);
    }

    private void validateItem(Item item) {
        if (item.getAvailable() == null) {
            String message = "Поле доступности должно быть заполнено";
            log.error(message);
            throw new ValidationException(message);
        }
        if (item.getName() == null || item.getName().isBlank()) {
            String message = "Название не может быть пустым или отсутствовать";
            log.error(message);
            throw new ValidationException(message);
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            String message = "Описание не может быть пустым или отсутствовать";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
