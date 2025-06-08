package com.bookie.scrap.watcha.request.user.userlikepeople;

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
public class UserLikePeoplePersister implements WatchaPersistFactory<UserLikePeopleResponseDto> {

    private final UserLikePeopleMongoRepository repository;

    @Override
    public int persist(UserLikePeopleResponseDto dto, String userCode) throws JsonProcessingException {

        List<JsonNode> userLikePeople = dto.getResult().getUserLikePeople();

        if (userLikePeople == null) {
            return 0;
        }

        log.debug("size: {}",userLikePeople.size());

        for (int idx = 0; idx < userLikePeople.size(); idx++) {

            log.debug(
                    "userLikePeoples idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(userLikePeople.get(idx))
            );
            log.debug("===========================");

            UserLikePeopleDocument document = new UserLikePeopleDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(userLikePeople.get(idx)));
            repository.save(document);
        }

        return userLikePeople.size();

    }
}
