package ru.fanur.exception;

import lombok.Getter;

@Getter
public class JwtFilterAuthException extends Exception {
    private static final String DEFAULT_PREFIX_MESSAGE = "Unauthorized error: ";

    public JwtFilterAuthException(String message) {
        super(DEFAULT_PREFIX_MESSAGE + message);
    }
}