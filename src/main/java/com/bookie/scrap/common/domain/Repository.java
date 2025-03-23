package com.bookie.scrap.common.domain;

import java.util.List;

public interface Repository<T> {

    List<T> selectWithCode(String code);

    boolean insertOrUpdate(T dto);

}
