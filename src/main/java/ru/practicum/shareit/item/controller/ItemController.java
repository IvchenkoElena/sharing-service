package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody Item item) {
        log.info("Вызван эндпоинт создания вещи");
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable("itemId") long itemId,
                           @RequestBody Item item) {
        log.info("Вызван эндпоинт обновления вещи с ID {}", itemId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вызван эндпоинт получения списка вещей");
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                            @PathVariable("itemId") long itemId) {
        log.info("Вызван эндпоинт получения вещи с ID {}", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> searchItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) long userId,
                                  @RequestParam("text") String text) {
        Optional<Long> mayBeUser = Optional.of(userId);
        log.info("Вызван эндпоинт поиска вещей");
        return itemService.searchItems(mayBeUser, text);
    }
}
