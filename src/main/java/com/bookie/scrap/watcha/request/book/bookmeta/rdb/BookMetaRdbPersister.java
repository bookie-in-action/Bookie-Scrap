package com.bookie.scrap.watcha.request.book.bookmeta.rdb;

import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookMetaRdbPersister implements WatchaPersistor<BookMetaResponseDto> {

    private final BookMetaRdbRepository repository;
    private final ObjectMapper mapper;

    @Override
    public int persist(BookMetaResponseDto dto, String bookCode) {
        try {
            BookMetaRdbDto bookMetaRdbDto = mapper.treeToValue(dto.getBookMeta(), BookMetaRdbDto.class);

            log.info("BookMeta RDB size: 1");

            BookMetaRdbEntity entity = bookMetaRdbDto.toEntity();
            repository.save(entity);
            log.info("bookCode: {} rdb meta saved", bookCode);

            return 1;
        } catch (JsonProcessingException e) {
            log.error("json 파싱 실패");
            return 0;
        }
    }
}
