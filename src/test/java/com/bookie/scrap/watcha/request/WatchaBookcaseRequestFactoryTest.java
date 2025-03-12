package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.common.startup.InitManager;
import com.bookie.scrap.watcha.domain.WatchaBaseRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class WatchaBookcaseRequestFactoryTest {

    @BeforeAll
    public static void init() {
        InitManager initManager = new InitManager();
        initManager.devInit();
    }

    @Test
    void createRequest() {
        RequestFactory<List<WatchaBookcaseDTO>> watchaBookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();

        WatchaBaseRequestParam watchaBaseRequestParam = new WatchaBaseRequestParam("gcdkyKnXjN", "1", "12");

        log.info("======================== testGetDetail Execute =============================");
        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest(watchaBaseRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();
        log.info("Result Data : {}", bookcaseList);
        log.info("======================== testGetDetail END =============================");

    }

//    void createRequest1() {
//        RequestFactory<List<WatchaBookcaseDTO>> watchaBookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();
//
//        int cnt = 1;
//        while (true) {
//            Request<List<WatchaBookcaseDTO>> bookcase = watchaBookcaseRequestFactory.createRequest(
//                    "gcdkyKnXjN",
//                    String.valueOf(cnt++),
//                    "15"
//            );
//            if (bookcase == null) {
//                break;
//            }
//        }
//    }
}

