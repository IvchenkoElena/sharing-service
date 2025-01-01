package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewItemRequestRequest {
    @NotNull
    private String description;
}
