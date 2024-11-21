package ru.practicum.shareit.item.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId((item.getId()));
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setOwner(item.getOwner());
        dto.setAvailable(item.getAvailable());
        dto.setRequest(item.getRequest());
        return dto;
    }
}
