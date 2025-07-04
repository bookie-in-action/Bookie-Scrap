package com.bookie.scrap.watcha.request.user.userwishbook;

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
public class UserWishBookPersister implements WatchaPersistor<UserWishBookResponseDto> {

    private final UserWishBookMongoRepository repository;

    @Override
    public int persist(UserWishBookResponseDto dto, String userCode) {

        List<JsonNode> userWishBooks = dto.getResult().getUserWishBooks();

        if (userWishBooks == null || userWishBooks.isEmpty()) {
            return 0;
        }

        log.info("UserWishBook size: {}",userWishBooks.size());

        List<UserWishBookDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < userWishBooks.size(); idx++) {

            try {
                UserWishBookDocument document = new UserWishBookDocument();
                document.setUserCode(userCode);
                document.setRawJson(JsonUtil.toMap(userWishBooks.get(idx)));
                documents.add(document);

                log.info("userCode: {} userWishBook idx: {} saved", userCode, count);
                log.debug(
                        "userWishBook idx: {}, value: {}",
                        count,
                        JsonUtil.toPrettyJson(userWishBooks.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.error("json 파싱 실패");
            }
        }

        repository.saveAll(documents);

        return userWishBooks.size();

    }
}
