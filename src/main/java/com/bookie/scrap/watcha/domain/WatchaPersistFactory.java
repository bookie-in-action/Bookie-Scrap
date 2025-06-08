package com.bookie.scrap.watcha.domain;

public interface WatchaPersistFactory<T> {
    int persist(T dto, String value) throws Exception;
}
