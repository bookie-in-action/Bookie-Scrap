package com.bookie.scrap.watcha.request.poc;

import com.bookie.legacy.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WatchaUserLikesBookcaseRequestFactoryTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void createRequest() {
        WatchaUserLikesBookcaseRequestFactory.getInstance().createRequest("ZWpqMekrDqrkn").execute();
    }
}