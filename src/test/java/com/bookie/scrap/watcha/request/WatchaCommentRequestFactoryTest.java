package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaCommentDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class WatchaCommentRequestFactoryTest {

    @BeforeAll
    public static void init() {
        InitManager initManager = new InitManager();
        initManager.devInit();
    }

    @Test
    void testWatchaCommentApiRequest() {
        WatchaCommentRequestFactory factory = WatchaCommentRequestFactory.getInstance();

        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 9, "", "");
        String bookCode = "byLKj8M";

        Request<List<WatchaCommentDto>> watchaRequest = factory.createRequest(bookCode, watchaRequestParam);
        List<WatchaCommentDto> commentList = watchaRequest.execute();
        log.info("Result Data : {}", commentList.get(4));
        log.info("Result Data : {}", commentList.get(4).toEntity());
    }
}
