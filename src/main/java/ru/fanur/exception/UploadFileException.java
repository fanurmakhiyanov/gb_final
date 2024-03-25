package ru.fanur.exception;


public class UploadFileException extends Exception{
    private static final String DEFAULT_PREFIX_MESSAGE = "Error upload file: ";

    public UploadFileException(String message) {
        super(DEFAULT_PREFIX_MESSAGE + message);
    }
}