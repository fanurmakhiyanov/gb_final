package ru.fanur.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginPasswordDtoIn(
        @NotBlank(message = "login is empty")
        String login,

        @NotBlank(message = "password is empty")
        String password
) {}