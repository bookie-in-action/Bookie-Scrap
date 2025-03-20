package com.bookie.scrap.watcha.domain;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

@Slf4j
public class WatchaRequestParam extends PageInfo {

    private final String filter;
    private final String order;

    public WatchaRequestParam(int page, int size, String filter, String order) {
        super(page, size);

        if (!StringUtil.hasText(filter)) {
            this.filter = "all";
        } else {
            this.filter = filter;
        }

        if (!StringUtil.hasText(order)) {
            this.order = "popular";
        } else {
            this.order = order;
        }

    }

    public String buildUrlWithParamInfo(String baseUrl) {
        super.validNotEmpty(baseUrl, "baseUrl");

        try {
            return new URIBuilder(baseUrl)
                    .addParameter("page", String.valueOf(super.page))
                    .addParameter("size", String.valueOf(super.size))
                    .addParameter("order", this.order)
                    .addParameter("filter", this.filter)
                    .build()
                    .toString();

        } catch (URISyntaxException e) {
            log.error("Failed to build URL for baseUrl {} with PramInfo: {}", baseUrl, getParamInfo());
            throw new IllegalStateException(e);
        }
    }


    public String getParamInfo() {
        return "WatchaRequestParam{" +
                "page=" + super.page +
                ", size=" + super.size +
                ", filter='" + filter + '\'' +
                ", order='" + order + '\'' +
                '}';
    }

}
