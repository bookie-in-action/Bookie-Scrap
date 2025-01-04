package com.bookie.scrap.watcha.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class WatchaBookDetailTest {

    @Test
    public void testPoster() throws JsonProcessingException {

        String json = "{"
                + "\"poster\": {"
                + "\"hd\": \"http://example.com/hd.jpg\","
                + "\"xlarge\": \"http://example.com/xlarge.jpg\","
                + "\"large\": \"http://example.com/large.jpg\","
                + "\"medium\": \"http://example.com/medium.jpg\","
                + "\"small\": \"http://example.com/small.jpg\""
                + "}"
                + "}";


        ObjectMapper objectMapper = new ObjectMapper();
        WatchaBookDetail detail = objectMapper.readValue(json, WatchaBookDetail.class);
        System.out.println(detail);
    }
}