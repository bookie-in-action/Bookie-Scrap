package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserBookRatingPersister implements WatchaPersistor<UserBookRatingResponseDto> {

    private final UserBookRatingMongoRepository repository;

    @Override
    public int persist(UserBookRatingResponseDto dto, String userCode) {

        List<JsonNode> bookRatings = dto.getResult().getBookRatings();

        if (bookRatings == null || bookRatings.isEmpty()) {
            return 0;
        }

        log.info("UserBookRatings size: {}",bookRatings.size());

        List<UserBookRatingDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < bookRatings.size(); idx++) {

            try {
                UserBookRatingDocument document = new UserBookRatingDocument();
                document.setUserCode(userCode);
                document.setRawJson(JsonUtil.toMap(bookRatings.get(idx)));
                documents.add(document);

                log.info("userCode: {} bookRating idx: {} saved", userCode, count);

                log.debug(
                        "userBookRating idx: {}, value: {}",
                        idx,
                        JsonUtil.toPrettyJson(bookRatings.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.error("json 파싱 실패");
            }
        }

        repository.saveAll(documents);

        return count;
    }
}
