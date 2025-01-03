package com.bookie.scrap.config.watcha;

import com.bookie.scrap.config.BaseRequestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

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
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                WatchaBookDetail bookDetail = objectMapper.treeToValue(
                        responseWrapper.getJsonNode().path("result"),
                        WatchaBookDetail.class
                );

                log.debug("=> Start searching for External Service URL [{}/{}]", bookDetail.getCode(), bookDetail.getTitle());
                List<String> redirectUrls = bookDetail.getExternalServices().stream().map(this::fetchWatchaRedirectUrl).collect(Collectors.toList());
                bookDetail.setUrlMap(mapExternalUrlsToTypes(redirectUrls));
                log.debug("<= End searching for External Service URL");

                log.debug("Parsed BookDetail: {}", bookDetail);

                return bookDetail;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            }
        };

        createResponseHandler(bookDetailFunction);
    }

    // 외부 URL 매핑 로직
    private Map<WatchaBookDetail.TYPE, String> mapExternalUrlsToTypes(List<String> redirectUrls) {

        Pattern pattern;
        Matcher matcher;

        Map<WatchaBookDetail.TYPE, String> urlMap = new HashMap<>();

        if (redirectUrls != null) {
            for (String href : redirectUrls) {

                if (href.contains("aladin")) {
                    pattern = Pattern.compile("(?<=ItemId=)(.*?)(?=&partner)");
                    matcher = pattern.matcher(href);
                    if (matcher.find()) {
                        urlMap.put(WatchaBookDetail.TYPE.ALADIN, "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=" + matcher.group(1));
                    }
                }

                else if (href.contains("yes24")){
                    urlMap.put(WatchaBookDetail.TYPE.YES24, href);
                }

                else if (href.contains("kyobobook")) {
                    //http://www.kyobobook.co.kr/cooper/redirect_over.jsp?LINK=WPD&next_url=https://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156&utm_source=watchapedia&utm_medium=posts&utm_campaign=9791189327156";
                    pattern = Pattern.compile("(?<=https://)(.*?)(?=&utm_source)");
                    matcher = pattern.matcher(href);

                    if (matcher.find()) {
                        //www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156
                        String kyoUrl = this.fetchAladinRedirectUrl("https://" + matcher.group(1));
                        urlMap.put(WatchaBookDetail.TYPE.KYOBO, kyoUrl);
                    }
                }

            }
        }

        return urlMap;
    }


    protected String fetchWatchaRedirectUrl(String requestUrl) {

        ClassicHttpRequest httpRequest =
                ClassicRequestBuilder.get(requestUrl)
                .addHeader("Referer", "https://pedia.watcha.com")
                .addHeader("X-Frograms-App-Code", "Galaxy")
                .addHeader("X-Frograms-Client", "Galaxy-Web-App")
                .addHeader("X-Frograms-Galaxy-Language", "ko")
                .addHeader("X-Frograms-Galaxy-Region", "KR")
                .addHeader("X-Frograms-Version", "2.1.0")
                .build();

        Function<HttpResponseWrapper, String> LocationHeaderResponse =
                responseWrapper -> responseWrapper.findHeader("location").get().getValue();

        return (String) HttpRequestExecutor.execute(httpRequest, getResponseHandler(LocationHeaderResponse));

    }

    protected String fetchAladinRedirectUrl(String requestUrl) {

        ClassicHttpRequest httpRequest = ClassicRequestBuilder.get(requestUrl).build();

        Function<HttpResponseWrapper, String> LocationHeaderResponse =
                responseWrapper -> responseWrapper.findHeader("location").get().getValue();

        return (String) HttpRequestExecutor.execute(httpRequest, getResponseHandler(LocationHeaderResponse));

    }
}
