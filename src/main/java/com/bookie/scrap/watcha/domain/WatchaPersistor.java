package com.bookie.scrap.watcha.domain;

public interface WatchaPersistor<T> {
    int persist(T dto, String value) throws Exception;
}
