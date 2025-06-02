package com.bookie.scrap.watcha.request.book.booktodecks;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookToDecksCollectionServiceTest {

    @Autowired
    private BookToDecksCollectionService service;

    @Test
    void collect() throws Exception {

        BookToDecksParam param = new BookToDecksParam(1, 10);

        service.collect("byLKj8M", param);
    }
}