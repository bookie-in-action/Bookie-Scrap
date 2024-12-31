package com.bookie.scrap.repository;

import com.bookie.scrap.dto.WatchaBookDTO;

import java.util.Optional;

public interface WatchaBookRepository {

    Optional<WatchaBookDTO> select(String bookCode);

    boolean update(WatchaBookDTO watchaBookDTO);

    String insert(WatchaBookDTO watchaBookDTO);

}
