package com.bookie.scrap.common.domain;

import java.util.Optional;

public interface Repository<T> {

    Optional<T> select(String code);

    boolean insertOrUpdate(T dto);

}
