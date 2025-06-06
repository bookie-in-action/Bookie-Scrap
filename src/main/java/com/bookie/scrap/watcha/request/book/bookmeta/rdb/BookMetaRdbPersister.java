package com.bookie.scrap.watcha.request.book.bookmeta.rdb;

import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookMetaRdbPersister implements WatchaPersistFactory<BookMetaResponseDto> {

    private final BookMetaRdbRepository repository;
    private final ObjectMapper mapper;

    @Override
    public void persist(BookMetaResponseDto dto, String bookCode) throws Exception {
        BookMetaRdbDto bookMetaRdbDto = mapper.treeToValue(dto.getBookMeta(), BookMetaRdbDto.class);

        if (bookMetaRdbDto == null) {
            throw new IllegalArgumentException("BookMetaRdbDto is null");
        }

        BookMetaRdbEntity entity = bookMetaRdbDto.toEntity();
        repository.save(entity);
    }
}
