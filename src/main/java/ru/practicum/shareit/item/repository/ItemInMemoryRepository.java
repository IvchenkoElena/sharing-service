package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class ItemInMemoryRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long currentId = 1;

    @Override
    public Item createItem(User owner, Item newItem) {
        newItem.setId(currentId);
        newItem.setOwner(owner);
        items.put(currentId, newItem);
        log.info("Новая вещь с ID {} добавлена в репозиторий", currentId);
        currentId += 1;
        return newItem;
    }

    @Override
    public Item updateItem(User owner, long itemId, Item updatedItem) {
        if (!items.containsKey(itemId)) {
            String message = "Вещь с ID " + itemId + " для обновления не обнаружена";
            log.error(message);
            throw new NotFoundException(message);
        }
        Item oldItem = items.get(itemId);
        if (oldItem.getOwner().getId() != owner.getId()) {
            String message = "У вещи с ID " + itemId + " другой владелец";
            log.error(message);
            throw new NotFoundException(message);
        }
        //обновляю поля
        if (updatedItem.getName() != null) {
            oldItem.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            oldItem.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            oldItem.setAvailable(updatedItem.getAvailable());
        }
        log.info("Вещь с ID {} обновлена в репозиторий", currentId);
        return oldItem;
    }

    @Override
    public List<Item> getAllItemsByOwnerId(long ownerId) {
        log.info("Выведен список вещей пользователя с ID {}", ownerId);
        return items.values().stream().filter(i -> i.getOwner().getId() == ownerId).toList();
    }

    @Override
    public Item getItemById(long ownerId, long itemId) {
        if (!items.containsKey(itemId)) {
            String message = "Вещь с ID " + itemId + " не обнаружена";
            log.error(message);
            throw new NotFoundException(message);
        }
        Item item = items.get(itemId);
        if (item.getOwner().getId() != ownerId) {
            String message = "У вещи с ID " + itemId + " другой владелец";
            log.error(message);
            throw new NotFoundException(message);
        }
        log.info("Найдена вещь с ID {}", itemId);
        return item;
    }

    @Override
    public List<Item> searchItems(Optional<Long> mayBeOwnerId, String text) {
        List<Item> searchItemsList;
        if (mayBeOwnerId.isPresent()) {
            long ownerId = mayBeOwnerId.get();
            searchItemsList = items.values().stream()
                    .filter(i -> i.getOwner().getId() == ownerId)
                    .filter(i -> i.getName().toUpperCase().contains(text.toUpperCase()))
                    .filter(i -> i.getAvailable())
                    .toList();
        } else {
            searchItemsList = items.values().stream()
                    .filter(i -> i.getName().toUpperCase().contains(text.toUpperCase()))
                    .filter(i -> i.getAvailable())
                    .toList();
        }
        log.info("Выведен результат поиска вещей");
        return searchItemsList;
    }
}
