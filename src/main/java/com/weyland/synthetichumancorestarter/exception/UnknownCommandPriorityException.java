package com.weyland.synthetichumancorestarter.exception;

public class UnknownCommandPriorityException extends RuntimeException {
    public UnknownCommandPriorityException(final String priority) {
        super("Unknown priority: " + priority);
    }
}
