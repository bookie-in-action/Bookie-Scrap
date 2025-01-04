package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.watcha.dto.WatchaBookDetailDTO;

import java.util.Optional;

public interface WatchaBookRepository {

    Optional<WatchaBookDetailDTO> select(String bookCode);

    boolean update(WatchaBookDetailDTO watchaBookDTO);

    Optional<String> insert(WatchaBookDetailDTO watchaBookDTO);

}
