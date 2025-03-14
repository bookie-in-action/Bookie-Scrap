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

    /** Watcha API 응답 객체가 예상된 응답 객체와 일치하는지 검증하는 테스트  */
    @Test
    void testWatchaApiResponse() {
        // Given: 테스트용 JSON 파일을 읽어 기대하는 응답 값 설정
        Path filePath = Paths.get("src/test/Test_bookcaseResponse.txt");

        String expectedJson = null;
        try {
            expectedJson = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("error occured : {}", e.getMessage());
            throw new RuntimeException(e);
        }

        // When : API 요청을 실행하여 실제 응답 데이터를 가져옴
        RequestFactory<List<WatchaBookcaseDTO>> watchaBookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();

        WatchaBaseRequestParam watchaBaseRequestParam = new WatchaBaseRequestParam("gcdkyKnXjN", "1", "12");

        Request<List<WatchaBookcaseDTO>> watchaRequest = watchaBookcaseRequestFactory.createRequest(watchaBaseRequestParam);
        List<WatchaBookcaseDTO> bookcaseList = watchaRequest.execute();
        log.info("bookCaseList : {}", bookcaseList);

        // Then: 예상된 JSON과 실제 응답을 비교
        Assertions.assertEquals(expectedJson, bookcaseList.toString(), "API response does not match the expected JSON.");
    }
}

