package ru.fanur.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginPasswordHash(
        @NotBlank
        @JsonProperty("filename")
        String filename
) {}