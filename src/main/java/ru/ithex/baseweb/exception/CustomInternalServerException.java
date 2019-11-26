package ru.ithex.baseweb.exception;

public class CustomInternalServerException extends RuntimeException {
    public CustomInternalServerException(String message) {
        super(message);
    }
}