package com.bookie.scrap.watcha.request.poc;

import com.bookie.legacy.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WatchaUserWishesPeopleRequestFactoryTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void createRequest() {
        WatchaUserWishesPeopleRequestFactory.getInstance().createRequest("6ADvGrJpKqzZl").execute();
    }
}