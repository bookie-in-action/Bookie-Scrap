package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

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
public class UserLikeDeckPersister implements WatchaPersistor<UserLikeDeckResponseDto> {

    private final UserLikeDeckMongoRepository repository;

    @Override
    public int persist(UserLikeDeckResponseDto dto, String userCode) throws JsonProcessingException {

        List<JsonNode> userWishPeople = dto.getResult().getUserWishPeople();

        if (userWishPeople == null || userWishPeople.size() == 0) {
            return 0;
        }

        log.debug("size: {}",userWishPeople.size());

        List<UserLikeDeckDocument> documents = new ArrayList<>();

        for (int idx = 0; idx < userWishPeople.size(); idx++) {

            log.debug(
                    "userWishPeoples idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(userWishPeople.get(idx))
            );
            log.debug("===========================");

            UserLikeDeckDocument document = new UserLikeDeckDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(userWishPeople.get(idx)));
            documents.add(document);
        }

        repository.saveAll(documents);

        return userWishPeople.size();

    }
}
