package com.store.demo.exception;

public class RoverException extends RuntimeException {

    public RoverException(String message, Throwable exception) {
        super(message, exception);
    }

    public RoverException(String message) {
        super(message);
    }

    public RoverException() {
        super();
    }
}
