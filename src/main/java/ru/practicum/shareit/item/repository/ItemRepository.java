package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(User owner, Item item);

    Item updateItem(User owner, long itemId, Item item);

    List<Item> getAllItemsByOwnerId(long ownerId);

    Item getItemById(long ownerId, long itemId);

    List<Item> searchItems(Optional<Long> ownerId, String text);
}
