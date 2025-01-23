package com.bookie.scrap.common.util;

import com.bookie.scrap.http.HttpResponseWrapper;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.function.Function;

public class ResponseHandlerMaker {

    public static <T> HttpClientResponseHandler<T> getWatchaHandlerTemplate(Function<HttpResponseWrapper, T> handlerFunction) {
        return httpResponse ->
        {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    httpResponse.getEntity()
            );

            httpResponseWrapper.printLog();

            int statusCode = httpResponseWrapper.getCode();
            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302 || statusCode == 301) {
                return handlerFunction.apply(httpResponseWrapper);
            } else {
                String exceptionMsg = String.format("Unexpected status code: ");
                throw new IllegalStateException(exceptionMsg + statusCode);
            }
        };
    }
}
