package com.bookie.scrap.watcha.config;

import com.bookie.scrap.watcha.type.WatchaBookType;
import com.bookie.scrap.watcha.type.WatchaBookType.EXTERNAL_SERVICE;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.watcha.response.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class WatchaBook extends WatchaBaseRequest<WatchaBookDetail> {

    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/contents/%s";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initHttpHost();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaBook(String bookCode) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode);
        initHttpMethod();
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
                List<String> redirectUrls = bookDetail.getExternalServices().stream()
                        .map(this::fetchWatchaRedirectUrl).collect(Collectors.toList());
                bookDetail.setUrlMap(mapExternalUrlsToTypes(redirectUrls));
                log.debug("<= End searching for External Service URL");

                log.debug("Parsed BookDetail: {}", bookDetail);

                return bookDetail;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            }
        };

        setResponseHandler(bookDetailFunction);
    }

    // 외부 URL 매핑 로직
    private Map<EXTERNAL_SERVICE, String> mapExternalUrlsToTypes(List<String> redirectUrls) {

        Matcher matcher;
        Map<EXTERNAL_SERVICE, String> urlMap = new HashMap<>();

        if (redirectUrls != null) {
            for (String href : redirectUrls) {
                if (href.contains("aladin")) {
                    matcher = Pattern.compile("(?<=ItemId=)(.*?)(?=&partner)").matcher(href);
                    if (matcher.find()) {
                        urlMap.put(EXTERNAL_SERVICE.ALADIN, "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=" + matcher.group(1));
                    }
                }

                else if (href.contains("yes24")){
                    urlMap.put(EXTERNAL_SERVICE.YES24, href);
                }

                else if (href.contains("kyobobook")) {
                    //http://www.kyobobook.co.kr/cooper/redirect_over.jsp?LINK=WPD&next_url=https://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156&utm_source=watchapedia&utm_medium=posts&utm_campaign=9791189327156";
                    matcher = Pattern.compile("(?<=https://)(.*?)(?=&utm_source)").matcher(href);
                    if (matcher.find()) {
                        String kyoUrl = this.fetchAladinRedirectUrl("https://" + matcher.group(1));
                        urlMap.put(EXTERNAL_SERVICE.KYOBO, kyoUrl);
                    }
                }

            }
        }

        return urlMap;
    }

    protected String fetchWatchaRedirectUrl(String requestUrl) {
        return fetchRedirectUrl(requestUrl, super.watchaHeaders);
    }

    protected String fetchAladinRedirectUrl(String requestUrl) {
        return fetchRedirectUrl(requestUrl, Map.of());
    }

    protected String fetchRedirectUrl(String requestUrl, Map<String, String> headers) {
        ClassicHttpRequest httpRequest = ClassicRequestBuilder.get(requestUrl).build();
        headers.forEach(httpRequest::addHeader);

        Function<HttpResponseWrapper, String> locationHeaderResponse = responseWrapper ->
                responseWrapper.findHeader("location").getValue();

        return HttpRequestExecutor.execute(httpRequest, createResponseHandler(locationHeaderResponse));
    }

}
