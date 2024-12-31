package com.bookie.scrap.repository;

import com.bookie.scrap.dto.WatchaBookDTO;

import java.util.Optional;

public class WatchaBookHibenateImpl implements WatchaBookRepository{
    @Override
    public Optional<WatchaBookDTO> select(String bookCode) {
        return Optional.empty();
    }

    @Override
    public boolean update(WatchaBookDTO watchaBookDTO) {
        return false;
    }

    @Override
    public String insert(WatchaBookDTO watchaBookDTO) {
        return "";
    }
}
