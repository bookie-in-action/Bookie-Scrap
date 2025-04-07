package com.bookie.scrap.watcha.request.poc;

import com.bookie.scrap.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchaUserAnalysisRequestFactoryTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void createRequest() {
        WatchaUserAnalysisRequestFactory.getInstance().createRequest("6ADvGrJpKqzZl").execute();
    }
}