package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseToBookDto;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
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
class WatchaBookcaseToBooksRequestFactoryTest {


    private static WatchaRequestFactory<List<WatchaBookcaseToBookDto>> watchaBookcaseRequestFactory;

    @BeforeAll
    public static void init() {
        InitManager initManager = new InitManager();
        initManager.devInit();
        watchaBookcaseRequestFactory = WatchaBookcaseToBooksRequestFactory.getInstance();
    }

    @Test
    void createRequest() {
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");

        Request<List<WatchaBookcaseToBookDto>> watchaRequest = watchaBookcaseRequestFactory.createRequest("gcdkyKnXjN", watchaRequestParam);
        List<WatchaBookcaseToBookDto> bookcaseList = watchaRequest.execute();
        log.info("Result Data : {}", bookcaseList);
    }

    @Test
    void transToEntity() {
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");

        Request<List<WatchaBookcaseToBookDto>> watchaRequest = watchaBookcaseRequestFactory.createRequest("gcdkyKnXjN", watchaRequestParam);
        List<WatchaBookcaseToBookDto> bookcaseList = watchaRequest.execute();
        // log.info("Result Data : {}", bookcaseList);

        for(WatchaBookcaseToBookDto dto : bookcaseList){
            // WatchaCommentEntity entity = WatchaCommentEntity.fromDTO(bookcaseList.get(i));
            WatchaBookcaseToBookEntity entity = WatchaBookcaseToBookDto.toEntity(dto);
            log.info("to Entity : {}", entity.toString());
        }
    }

    //TODO: 아현님이랑 상의
//    @Test
//    void testWatchaApiResponse() {
//        Path filePath = Paths.get("src/test/Test_bookcaseResponse.txt");
//
//        String expectedJson = null;
//        try {
//            expectedJson = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            log.error("error occured in read files : {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");
//
//        Request<List<WatchaBookcaseToBookDto>> watchaRequest = watchaBookcaseRequestFactory.createRequest("gcdkyKnXjN", watchaRequestParam);
//        List<WatchaBookcaseToBookDto> bookcaseList = watchaRequest.execute();
//        log.info("bookCaseList : {}", bookcaseList);
//
//        Assertions.assertEquals(expectedJson, bookcaseList.toString());
//    }

    @Test
    void endPageTest() {
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(99, 12, "", "");

        Request<List<WatchaBookcaseToBookDto>> watchaRequest = watchaBookcaseRequestFactory.createRequest("gcdkyKnXjN", watchaRequestParam);
        List<WatchaBookcaseToBookDto> bookcaseList = watchaRequest.execute();

        Assertions.assertTrue(bookcaseList.isEmpty());
    }
}

