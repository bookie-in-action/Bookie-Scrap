package com.bookie.legacy.watcha.request;

import com.bookie.legacy.common.domain.Request;
import com.bookie.legacy.common.lifecycle.InitManager;
import com.bookie.legacy.watcha.domain.WatchaRequestParam;
import com.bookie.legacy.watcha.dto.WatchaCommentDto;
import com.bookie.legacy.watcha.request.WatchaCommentRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
@Disabled
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
