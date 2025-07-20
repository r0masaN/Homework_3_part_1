package com.weyland.synthetichumancorestarter.exception;

public class CommandQueueFullException extends RuntimeException {
    public CommandQueueFullException(final String message) {
        super(message);
    }
}
