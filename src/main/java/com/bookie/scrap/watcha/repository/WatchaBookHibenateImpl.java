package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.dto.WatchaBookDetailDTO;
import com.bookie.scrap.watcha.response.WatchaBookDetail;

import java.util.Optional;

public class WatchaBookHibenateImpl implements WatchaBookRepository {

    @Override
    public Optional<WatchaBookDetailDTO> select(String bookCode) {
        return Optional.empty();
    }

    @Override
    public boolean update(WatchaBookDetailDTO watchaBookDTO) {
        return false;
    }

    @Override
    public Optional<String> insert(WatchaBookDetailDTO watchaBookDTO) {
        return Optional.empty();
    }
}
