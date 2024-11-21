package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item createItem(long ownerId, Item item);

    Item updateItem(long ownerId, long itemId, Item item);

    List<Item> getAllItems(long ownerId);

    Item getItemById(long ownerId, long itemId);

    List<Item> searchItems(Optional<Long> mayBeUser, String text);
}
