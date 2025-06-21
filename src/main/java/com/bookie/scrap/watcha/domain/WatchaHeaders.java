package com.bookie.scrap.watcha.domain;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

public class WatchaHeaders {

    @Getter
    private static final HttpHeaders headers;
    static {
        headers = new HttpHeaders();
        headers.setAll(Map.of(
                "Referer", "https://pedia.watcha.com",
                "X-Frograms-App-Code", "Galaxy",
                "X-Frograms-Client", "Galaxy-Web-App",
                "X-Frograms-Galaxy-Language", "ko",
                "X-Frograms-Galaxy-Region", "KR",
                "X-Frograms-Version", "2.1.0"
        ));
    }
}
