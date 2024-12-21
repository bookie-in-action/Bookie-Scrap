package com.bookie.scrap.config.watcha;

import com.bookie.scrap.config.BaseRequestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Getter
public class WatchaBookConfig extends BaseRequestConfig<WatchaBookDetail> {

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

        Function<HttpResponseWrapper, WatchaBookDetail> bookDetailFunction = responseWrapper -> {

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


            log.debug("=> Start searching for External Service URL [{}/{}]", code, title );
            /*
                1. external_services가 array로 되어 있다면
                2. array를 stream으로 만든다
                3. array안의 node 1개 = 알라딘/yes24/교보문고 중 1이기에 요청 전송하여 url 가져오기
                4. 알라딘/yes24/교보문고 url을 리스트로 만들고 반환되는 url이 없다면 빈 리스트를 만든다
            */
            List<String> externalUrls =
                    Optional.ofNullable(resultNode.path("external_services")).filter(JsonNode::isArray)
                    .map(arrayNode ->
                            StreamSupport.stream(arrayNode.spliterator(), false)
                                    .map(node -> {
                                        String href = node.path("href").asText();
                                        String[] splitUrl = href.split("/");
                                        return HttpRequestExecutor.execute(new WatchaExternalUrlConfig(splitUrl[4]));
                                    }).collect(Collectors.toList())
                    ).orElse(Collections.emptyList());

            Map<WatchaBookDetail.TYPE, String> urlMap = new HashMap<>();
            externalUrls.forEach(url -> {
                Pattern pattern;
                Matcher matcher;

                // www.naver.com -> naver 골라내기
                pattern = Pattern.compile("(?<=www\\.)(.*?)(?=\\.)");
                matcher = pattern.matcher(url);

                if (matcher.find()) {
                    switch (matcher.group(1)) {
                        case "aladin":
                            pattern = Pattern.compile("(?<=ItemId=)(.*?)(?=&partner)");
                            matcher = pattern.matcher(url);
                            if (matcher.find()) {
                                urlMap.put(WatchaBookDetail.TYPE.ALADIN, "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=" + matcher.group(1));
                            }
                            break;
                        case "yes24":
                            urlMap.put(WatchaBookDetail.TYPE.YES24, url);
                            break;
                        case "kyobobook":
                            //http://www.kyobobook.co.kr/cooper/redirect_over.jsp?LINK=WPD&next_url=https://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156&utm_source=watchapedia&utm_medium=posts&utm_campaign=9791189327156";
                            pattern = Pattern.compile("(?<=https://)(.*?)(?=&utm_source)");
                            matcher = pattern.matcher(url);

                            if (matcher.find()) {
                                //www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156
                                String[] splitUrl = matcher.group(1).split("/", 2);
                                String kyoUrl = HttpRequestExecutor.execute(new KyoboUrlConfig(splitUrl[0], "/" + splitUrl[1]));
                                urlMap.put(WatchaBookDetail.TYPE.KYOBO, kyoUrl);
                            }
                            break;
                    }
                }
            });

            log.debug("<= End searching for External Service URL");

            return new WatchaBookDetail(
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
                    urlMap,
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
