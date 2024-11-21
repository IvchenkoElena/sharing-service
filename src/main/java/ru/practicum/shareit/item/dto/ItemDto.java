package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Positive
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    User owner;
    @NotNull
    Boolean available;
    ItemRequest request;
}
