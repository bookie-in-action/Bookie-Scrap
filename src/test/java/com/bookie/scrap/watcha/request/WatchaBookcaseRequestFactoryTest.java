package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.dto.WatchaBaseRequestParamDTO;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.type.WatchaBookType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
class WatchaBookcaseRequestFactoryTest {
    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

    }

    @Test
    void createRequest() {
        RequestFactory<List<WatchaBookcaseDTO>> watchaBookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();

        WatchaBaseRequestParamDTO watchaBaseRequestParamDTO = new WatchaBaseRequestParamDTO("gcdkyKnXjN", "1", "12");

        log.info("======================== testGetDetail Execute =============================");
        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest(watchaBaseRequestParamDTO);
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

