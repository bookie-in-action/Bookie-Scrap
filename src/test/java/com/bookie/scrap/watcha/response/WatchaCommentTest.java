package com.bookie.scrap.watcha.response;

import com.bookie.scrap.common.DatabaseConnectionPool;
import com.bookie.scrap.http.HttpClientProvider;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.service.WatchaCommentService;
import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WatchaCommentTest {

    private static final Logger log = LoggerFactory.getLogger(WatchaCommentTest.class);
    WatchaCommentService watchaCommentService = new WatchaCommentService();

    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.stream().forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

        log.info("[T-STEP 2] DB Pool initialize");
        DatabaseConnectionPool.getInstance().init();

        log.info("[T-STEP 3] Http Connection Pool initialize");
        HttpClientProvider.init();
    }

    @Test
    public void testGetDetail() {
        log.info("======================== testGetDetail Execute =============================");
        // select() 호출
        String bookCode = "T-6NW5Q33oAq1Yo";
        Optional<WatchaCommentDetailDTO> result = watchaCommentService.getDetail(bookCode);
        log.info("Result Data : {}", result);
        log.info("======================== testGetDetail END =============================");

    }
}
