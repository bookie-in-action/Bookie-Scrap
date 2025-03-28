package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
class WatchaBookToBookcaseMetasRequestFactoryTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void createRequestUriTest() throws URISyntaxException {
        WatchaBookToBookcaseMetasRequestFactory factory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest("qwer", requestParam);

        Assertions.assertEquals(
                "https://pedia.watcha.com/api/contents/qwer/decks?page=1&size=12",
                request.getMainRequest().getUri().toString()
        );
    }

    @Test
    void pageNationNextPageTest() {
        WatchaBookToBookcaseMetasRequestFactory factory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
        String bookCode = "byLKj8M";
//        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest(bookCode, requestParam);
        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest(bookCode);

        List<WatchaBookcaseMetaDto> executedList1 = request.execute();

        requestParam.nextPage();
        Request<List<WatchaBookcaseMetaDto>> request1 = factory.createRequest(bookCode, requestParam);
        List<WatchaBookcaseMetaDto> executeList2 = request1.execute();

        Assertions.assertNotEquals(executedList1.get(0), executeList2.get(0));

    }

    @Test
    void pageNationTest() {
        WatchaBookToBookcaseMetasRequestFactory factory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 1, "", "");
        String bookCode = "byLKj8M";
        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest(bookCode, requestParam);
        List<WatchaBookcaseMetaDto> executedResult = request.execute();

        Assertions.assertEquals(1, executedResult.size());

    }

    @Test
    void requestExactTest() {
        WatchaBookToBookcaseMetasRequestFactory factory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 1, "", "");
        String bookCode = "byLKj8M";
        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest(bookCode, requestParam);
        WatchaBookcaseMetaDto result = request.execute().get(0);

        Assertions.assertEquals("gcd9rBe6w9", result.getBookcaseCode());
        Assertions.assertEquals("윤보나", result.getUser().getUserName());
        Assertions.assertEquals("ZBm5RYXEnvd46", result.getUser().getUserCode());
        Assertions.assertNotNull(result.getUser().getUserPhoto().getLarge());
    }

    @Test
    void endOfPageTest() {
        WatchaBookToBookcaseMetasRequestFactory factory = WatchaBookToBookcaseMetasRequestFactory.getInstance();
        WatchaRequestParam requestParam = new WatchaRequestParam(100, 20, "", "");
        String bookCode = "byLKj8M";
        Request<List<WatchaBookcaseMetaDto>> request = factory.createRequest(bookCode, requestParam);
        List<WatchaBookcaseMetaDto> result = request.execute();

        Assertions.assertEquals(0, result.size());
    }


}