package com.bookie.scrap.common.domain;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface Repository<T> {

    List<T> selectWithCode(String code, EntityManager em);

}