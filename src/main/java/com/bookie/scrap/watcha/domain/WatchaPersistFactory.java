package com.bookie.scrap.watcha.domain;

public interface WatchaPersistFactory<T> {
    void persist(T dto, String value) throws Exception;
}
