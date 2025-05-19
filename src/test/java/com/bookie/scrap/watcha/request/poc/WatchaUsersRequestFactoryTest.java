package com.bookie.scrap.watcha.request.poc;

import com.bookie.legacy.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WatchaUsersRequestFactoryTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void createRequest() {
        WatchaUsersRequestFactory.getInstance().createRequest("ZWpqMekrDqrkn").execute();
    }
}