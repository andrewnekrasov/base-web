package ru.ithex.baseweb.exception;

public class RestServiceSystemException extends RuntimeException {
    public RestServiceSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
