package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.HttpResponseUtil;
import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import com.bookie.scrap.watcha.type.WatchaBookType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookReponseHandler {


    public static HttpClientResponseHandler<WatchaBookDto> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    private static Function<HttpEntity, WatchaBookDto> createHandlerLogic() {


        return httpEntity -> {

            try {

                WatchaBookDto bookDetail =
                        ObjectMapperUtil.readValue(EntityUtils.toString(httpEntity), Response.class).getWatchaBookDto();


                log.debug("=> Start searching for External Service URL [{}/{}]", bookDetail.getBookCode(), bookDetail.getMainTitle());
                List<String> redirectUrls = bookDetail.getExternalServices().stream()
                        .map(WatchaBookReponseHandler::fetchWatchaRedirectUrl).collect(Collectors.toList());

                bookDetail.setUrlMap(mapExternalUrlsToTypes(redirectUrls));
                log.debug("<= End searching for External Service URL");

                log.debug("Parsed BookDetail: {}", bookDetail);

                return bookDetail;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        };

    }

    // 외부 URL 매핑 로직
    private static Map<WatchaBookType.EXTERNAL_SERVICE, String> mapExternalUrlsToTypes(List<String> redirectUrls) {

        Matcher matcher;
        Map<WatchaBookType.EXTERNAL_SERVICE, String> urlMap = new HashMap<>();

        if (redirectUrls != null) {
            for (String href : redirectUrls) {
                if (href.contains("aladin")) {
                    matcher = Pattern.compile("(?<=ItemId=)(.*?)(?=&partner)").matcher(href);
                    if (matcher.find()) {
                        urlMap.put(WatchaBookType.EXTERNAL_SERVICE.ALADIN, "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=" + matcher.group(1));
                    }
                }

                else if (href.contains("yes24")){
                    urlMap.put(WatchaBookType.EXTERNAL_SERVICE.YES24, href);
                }

                else if (href.contains("kyobobook")) {
                    //http://www.kyobobook.co.kr/cooper/redirect_over.jsp?LINK=WPD&next_url=https://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156&utm_source=watchapedia&utm_medium=posts&utm_campaign=9791189327156";
                    matcher = Pattern.compile("(?<=https://)(.*?)(?=&utm_source)").matcher(href);
                    if (matcher.find()) {
                        String kyoUrl = WatchaBookReponseHandler.fetchAladinRedirectUrl("https://" + matcher.group(1));
                        urlMap.put(WatchaBookType.EXTERNAL_SERVICE.KYOBO, kyoUrl);
                    }
                }

            }
        }

        return urlMap;
    }

    private static String fetchWatchaRedirectUrl(String requestUrl) {
        return WatchaBookReponseHandler.fetchRedirectUrl(requestUrl, WatchaRequest.getWATCHA_HEADERS());
    }

    private static String fetchAladinRedirectUrl(String requestUrl) {
        return WatchaBookReponseHandler.fetchRedirectUrl(requestUrl, Map.of());
    }

    private static String fetchRedirectUrl(String requestUrl, Map<String, String> headers) {
        ClassicHttpRequest httpRequest = ClassicRequestBuilder.get(requestUrl).build();
        headers.forEach(httpRequest::addHeader);

        HttpClientResponseHandler<String> locationHandler =
                WatchaHandlerTemplate.createTemplateWithHeaders(
                        responseHeaders -> {
                            Header locationHeader = HttpResponseUtil.findHeader(responseHeaders, "location")
                                    .orElseThrow(() -> new IllegalArgumentException("cannot get location header from " + requestUrl));

                            return locationHeader.getValue();
                        }
                );

        return HttpRequestExecutor.execute(httpRequest, locationHandler);
    }

    private static class Response {
        @Getter
        @JsonProperty("result")
        private WatchaBookDto watchaBookDto;
    }
}
