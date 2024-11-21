package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    @Positive
    long id;
    @NotBlank
    String name;
    @Email
    @NotBlank
    String email;
}
