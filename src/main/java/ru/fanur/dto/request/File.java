package ru.fanur.dto.request;

import jakarta.validation.constraints.NotBlank;

public record File(
        @NotBlank(message = "file is empty")
        String file
) { }