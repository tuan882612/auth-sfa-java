package com.authsfa.web.exception;

public class InvalidCredException extends Exception {
    public InvalidCredException(String message) {
        super(message);
    }

    public InvalidCredException(String message, Throwable cause) {
        super(message, cause);
    }
}
