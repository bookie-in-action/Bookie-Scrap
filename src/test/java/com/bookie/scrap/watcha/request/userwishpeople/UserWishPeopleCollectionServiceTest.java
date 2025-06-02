package com.bookie.scrap.watcha.request.userwishpeople;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWishPeopleCollectionServiceTest {

    @Autowired
    private UserWishPeopleCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 10);

        service.collect("JgAx8wnyY5LbO", requestParam);
    }
}