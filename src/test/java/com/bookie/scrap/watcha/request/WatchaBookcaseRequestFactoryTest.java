package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.domain.WatchaBaseRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        WatchaBaseRequestParam watchaBaseRequestParam = new WatchaBaseRequestParam("gcdkyKnXjN", "1", "12");

        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest(watchaBaseRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();

        log.info("bookCaseList : {}", bookcaseList);
    }
}

