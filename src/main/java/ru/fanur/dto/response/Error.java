package ru.fanur.dto.response;

public record Error(
        String message,
        Integer id
) {}