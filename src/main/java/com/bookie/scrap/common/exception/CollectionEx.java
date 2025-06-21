package com.bookie.scrap.common.exception;

public class CollectionEx extends RuntimeException {

    public CollectionEx(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionEx(String message) {
        super(message);
    }

    public static CollectionEx MAKE(String message) {
        return new CollectionEx(message);
    }

    public static CollectionEx MAKE(String message, Throwable cause) {
        return new CollectionEx(message, cause);
    }
}
