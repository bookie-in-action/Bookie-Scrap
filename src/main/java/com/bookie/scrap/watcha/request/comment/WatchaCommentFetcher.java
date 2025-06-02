package com.bookie.scrap.watcha.request.comment;

import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import com.bookie.scrap.common.http.WebClientExecutor;
import com.bookie.scrap.watcha.domain.WatchaHttpFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.unix.X11;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WatchaCommentFetcher implements WatchaHttpFactory<String> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;

    private static final HttpHeaders WATCHA_HEADERS;
    static {
        WATCHA_HEADERS = new HttpHeaders();
        WATCHA_HEADERS.setAll(Map.of(
                "Referer", "https://pedia.watcha.com",
                "X-Frograms-App-Code", "Galaxy",
                "X-Frograms-Client", "Galaxy-Web-App",
                "X-Frograms-Galaxy-Language", "ko",
                "X-Frograms-Galaxy-Region", "KR",
                "X-Frograms-Version", "2.1.0"
        ));
    }
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/comments?";

    private final WatchaCommentMongoRepository repository;

    @Override
    public void execute(String bookCode, WatchaRequestParam param) {

        String endpoint = param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, bookCode));

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        try {

            WatchaCommentResponseWrapper wrapper = mapper.readValue(rawJson, WatchaCommentResponseWrapper.class);

            List<Object> comments = wrapper.getResult().getComments();

            log.debug("size: {}",comments.size());

            for (int idx = 0; idx < comments.size(); idx++) {

                log.debug(
                        "comment idx: {}, value: {}",
                        idx,
                        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments.get(idx))
                );
                log.debug("===========================");

                WatchaCommentDocument document = new WatchaCommentDocument();
                document.setBookCode(bookCode);
                document.setRawJson(comments.get(idx).toString());
                repository.save(document);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SpringRequest createSpringRequest(String endpoint) {
        return SpringRequest.builder()
                .method(HTTP_METHOD)
                .url(endpoint)
                .headers(WATCHA_HEADERS)
                .build();
    }

    @Override
    public SpringResponse<String> createSpringResponse() {
        return new SpringResponse<>(response -> response.bodyToMono(String.class));
    }


    private static class WatchaCommentResponseWrapper {

        @JsonProperty("metadata")
        private Object metaData;

        @Getter
        private InnerResult result;

        private static class InnerResult {
            @JsonProperty("prev_uri")
            private String prevUri;

            @JsonProperty("next_uri")
            private String nextUri;

            @Getter
            @JsonProperty("result")
            private List<Object> comments;
        }
    }
}

