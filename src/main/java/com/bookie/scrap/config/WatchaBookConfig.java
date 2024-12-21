package com.bookie.scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.response.BookDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Getter
public class WatchaBookConfig extends BaseRequestConfig<BookDetail> {

    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/contents/";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        createHttpHost();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaBookConfig(String bookCode) {
        initWatchaHttpMethod(bookCode);
        initCustomHandler();
    }

    private void initCustomHandler() {

        Function<HttpResponseWrapper, BookDetail> bookDetailFunction = responseWrapper -> {

            JsonNode resultNode = responseWrapper.getJsonNode().path("result");

            String code = resultNode.path("code").asText();
            String title = resultNode.path("title").asText();
            List<String> authors = Optional.ofNullable(resultNode.path("author_names"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            List<String> nations = Optional.ofNullable(resultNode.path("nations"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(node -> node.path("name").asText())
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            List<String> genres = Optional.ofNullable(resultNode.path("genres"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());


            /*
                1. external_services가 array로 되어 있다면
                2. array를 stream으로 만든다
                3. array안의 node 1개 = 알라딘/yes24/교보문고 중 1이기에 요청 전송하여 url 가져오기
                4. 알라딘/yes24/교보문고 url을 리스트로 만들고 반환되는 url이 없다면 빈 리스트를 만든다
            */
            List<String> externalUrls =
                    Optional.ofNullable(resultNode.path("external_services")).filter(JsonNode::isArray)
                    .map(arrayNode ->
                            StreamSupport.stream(arrayNode.spliterator(), false).map(node -> {
                                log.debug("=> Start searching for {} URL [{}/{}]", node.path("name").asText(), code, title );

                                String href = node.path("href").asText();
                                String[] splitUrl = href.split("/");
                                return HttpRequestExecutor.execute(new WatchaExternalUrlConfig(splitUrl[4]));
                            }).collect(Collectors.toList())).orElse(Collections.emptyList());

            return new BookDetail(
                    code,
                    title,
                    resultNode.path("subtitle").asText(),
                    resultNode.path("content").asText(),
                    resultNode.path("year").asText(),
                    resultNode.path("poster").path("hd").asText(),
                    resultNode.path("poster").path("xlarge").asText(),
                    resultNode.path("poster").path("large").asText(),
                    resultNode.path("poster").path("medium").asText(),
                    resultNode.path("poster").path("small").asText(),
                    authors,
                    nations,
                    genres,
                    externalUrls,
                    resultNode.path("description").asText(),
                    resultNode.path("publisher_description").asText(),
                    resultNode.path("author_description").asText(),
                    resultNode.path("ratings_agv").asText(),
                    resultNode.path("ratings_count").asText(),
                    resultNode.path("wishes_count").asText()
            );
        };

        createResponseHandler(bookDetailFunction);
    }


}
