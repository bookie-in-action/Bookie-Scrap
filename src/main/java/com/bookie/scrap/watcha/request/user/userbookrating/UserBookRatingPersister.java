package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserBookRatingPersister implements WatchaPersistFactory<UserBookRatingResponseDto> {

    private final UserBookRatingMongoRepository repository;

    @Override
    public int persist(UserBookRatingResponseDto dto, String userCode) throws JsonProcessingException {

        List<JsonNode> bookRatings = dto.getResult().getBookRatings();

        if (bookRatings == null) {
            return 0;
        }

        log.debug("size: {}",bookRatings.size());

        for (int idx = 0; idx < bookRatings.size(); idx++) {

            log.debug(
                    "userBookRating idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(bookRatings.get(idx))
            );
            log.debug("===========================");

            UserBookRatingDocument document = new UserBookRatingDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(bookRatings.get(idx)));
            repository.save(document);
        }

        return bookRatings.size();
    }
}
