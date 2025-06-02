package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.domain.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

@Slf4j
public class WatchaBookCommentParam extends PageInfo {

    private String filter;
    private String order;

    public WatchaBookCommentParam(Void noUse) {
        super(1, 1);
    }

    public WatchaBookCommentParam(int page, int size) {
        super(page, size);
    }

    public void setAllFilter() {
        this.filter = "all";
    }

    /**
     * 좋아요 순
     */
    public void setPopularOrder() {
        this.order = "popular";
    }

    /**
     * 유저 반응 순
     */
    public void setRecommendedOrder() {
        this.order = "recommended";
    }

    /**
     * 높은 평가 순
     */
    public void setHighOrder() {
        this.order = "high";
    }

    /**
     * 낮은 평가 순
     */
    public void setLowOrder() {
        this.order = "low";
    }

    /**
     * 작성 순
     */
    public void setRecentOrder() {
        this.order = "recent";
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
        return "WatchaCommentParam{" +
                "page=" + super.page +
                ", size=" + super.size +
                ", filter='" + filter + '\'' +
                ", order='" + order + '\'' +
                '}';
    }

}
