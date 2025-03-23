package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
class WatchaBookcaseRequestFactoryTest {

    private static WatchaRequestFactory<List<WatchaBookcaseDTO>> watchaBookcaseRequestFactory;

    @BeforeAll
    public static void init() {
        InitManager initManager = new InitManager();
        initManager.devInit();
        watchaBookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();
    }

    @Test
    void createRequest() {

//        WatchaRequestParam watchaRequestParam = new WatchaRequestParam("gcdkyKnXjN", 1, 12);
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");

        log.info("======================== testGetDetail Execute =============================");
        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest("", watchaRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();
        log.info("Result Data : {}", bookcaseList);
        log.info("======================== testGetDetail END =============================");

    }

    @Test
    void testWatchaApiResponse() {
        Path filePath = Paths.get("src/test/Test_bookcaseResponse.txt");

        String expectedJson = null;
        try {
            expectedJson = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("error occured in read files : {}", e.getMessage());
            throw new RuntimeException(e);
        }

        // WatchaBookcase Response
//        WatchaRequestParam watchaRequestParam = new WatchaRequestParam("gcdkyKnXjN", 1, 12);
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");

        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest("", watchaRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();
        log.info("bookCaseList : {}", bookcaseList);

        Assertions.assertEquals(expectedJson, bookcaseList.toString());
    }

    @Test
    void endPageTest() {

//        WatchaRequestParam watchaRequestParam = new WatchaRequestParam("gcdkyKnXjN", 1, 12);
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");

        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest("", watchaRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();

        Assertions.assertTrue(bookcaseList.isEmpty());
    }
}

