package com.bookie.scrap.watcha.request.user.userlikepeople;

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
public class UserLikePeoplePersister implements WatchaPersistor<UserLikePeopleResponseDto> {

    private final UserLikePeopleMongoRepository repository;

    @Override
    public int persist(UserLikePeopleResponseDto dto, String userCode) {

        List<JsonNode> userLikePeople = dto.getResult().getUserLikePeople();

        if (userLikePeople == null || userLikePeople.isEmpty()) {
            return 0;
        }

        log.info("UserLikePeople size: {}",userLikePeople.size());

        List<UserLikePeopleDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < userLikePeople.size(); idx++) {
            try {

                UserLikePeopleDocument document = new UserLikePeopleDocument();
                document.setUserCode(userCode);
                document.setRawJson(JsonUtil.toMap(userLikePeople.get(idx)));
                documents.add(document);

                log.info("userCode: {} userLikePeople count: {} saved", userCode, count);
                log.debug(
                        "userLikePeoples idx: {}, value: {}",
                        idx,
                        JsonUtil.toPrettyJson(userLikePeople.get(idx))
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
