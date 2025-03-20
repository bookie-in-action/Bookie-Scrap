package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;

import java.util.Optional;

public interface WatchaCommentRepository {

    Optional<WatchaCommentDetailDTO> select(String bookCode);

    boolean update(WatchaCommentDetailDTO watchaCommentDTO);

    Optional<String> insert(WatchaCommentDetailDTO watchaCommentDTO);

}
