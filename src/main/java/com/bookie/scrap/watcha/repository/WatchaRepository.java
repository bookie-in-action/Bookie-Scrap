package com.bookie.scrap.watcha.repository;

import java.util.Optional;

public interface WatchaRepository<T> {

    Optional<T> select(String code);

    boolean insertOrUpdate(T dto);

}
