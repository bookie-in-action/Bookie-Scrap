package com.bookie.scrap.common.exception;

public class WatchaCustomCollectionEx extends Exception {

    public WatchaCustomCollectionEx(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public WatchaCustomCollectionEx(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public WatchaCustomCollectionEx(String message) {
        super(message);
    }
}
