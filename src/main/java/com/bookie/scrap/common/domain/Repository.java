package com.bookie.scrap.common.domain;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    List<T> select(String code);

    boolean insertOrUpdate(T dto);

}
