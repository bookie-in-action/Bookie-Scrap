package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserWishBookPersister implements WatchaPersistFactory<UserWishBookResponseDto> {

    private final UserWishBookMongoRepository repository;

    @Override
    public int persist(UserWishBookResponseDto dto, String userCode) {

        List<JsonNode> userWishBooks = dto.getResult().getUserWishBooks();

        if (userWishBooks == null || userWishBooks.size() == 0) {
            return 0;
        }

        log.debug("size: {}",userWishBooks.size());

        List<UserWishBookDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < userWishBooks.size(); idx++) {

            try {
                UserWishBookDocument document = new UserWishBookDocument();
                document.setUserCode(userCode);
                document.setRawJson(JsonUtil.toMap(userWishBooks.get(idx)));
                documents.add(document);

                log.debug(
                        "userWishBook idx: {}, value: {}",
                        idx,
                        JsonUtil.toPrettyJson(userWishBooks.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.warn("json 파싱 실패");
            }
        }

        repository.saveAll(documents);

        return userWishBooks.size();

    }
}
