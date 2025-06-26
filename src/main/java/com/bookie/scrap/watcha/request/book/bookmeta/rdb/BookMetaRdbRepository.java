package com.bookie.scrap.watcha.request.book.bookmeta.rdb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookMetaRdbRepository extends JpaRepository<BookMetaRdbEntity, String> {
}