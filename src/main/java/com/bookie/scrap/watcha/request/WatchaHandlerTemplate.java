package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.HttpResponseUtil;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.BufferedHttpEntity;

import java.util.function.Function;

public class WatchaHandlerTemplate {

    public static <T> HttpClientResponseHandler<T> createTemplateWithEntity(Function<HttpEntity, T> handlerFunction) {
        return httpResponse ->

        {

            int statusCode = httpResponse.getCode();
            HttpEntity copiedEntity = new BufferedHttpEntity(httpResponse.getEntity());

            HttpResponseUtil.printLog(copiedEntity, httpResponse.getHeaders(), statusCode);

            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302 || statusCode == 301 || statusCode == 308) {
                return handlerFunction.apply(copiedEntity);
            } else {
                String exceptionMsg = String.format("Unexpected status code: ");
                throw new IllegalStateException(exceptionMsg + statusCode);
            }

        };

    }

    public static <T> HttpClientResponseHandler<T> createTemplateWithHeaders(Function<Header[], T> handlerFunction) {
        return httpResponse ->

        {

            int statusCode = httpResponse.getCode();
            HttpEntity copiedEntity = new BufferedHttpEntity(httpResponse.getEntity());

            HttpResponseUtil.printLog(copiedEntity, httpResponse.getHeaders(), statusCode);

            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302 || statusCode == 301 || statusCode == 308) {
                return handlerFunction.apply(httpResponse.getHeaders());
            } else {
                String exceptionMsg = String.format("Unexpected status code: ");
                throw new IllegalStateException(exceptionMsg + statusCode);
            }

        };
    }


}
