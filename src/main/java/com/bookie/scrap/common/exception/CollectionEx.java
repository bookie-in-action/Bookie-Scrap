package com.bookie.scrap.common.exception;

public class CollectionEx extends RuntimeException {

    public CollectionEx(String message, Throwable cause) {
        super(message, cause);
//        this.setStackTrace(cause.getStackTrace());
    }

    public CollectionEx(Throwable cause) {
        super(cause);
//        this.setStackTrace(cause.getStackTrace());
    }

}
