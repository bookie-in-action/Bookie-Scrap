package com.bookie.scrap.watcha.request.user.userwishpeople;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserWishPeoplePersister implements WatchaPersistFactory<UserWishPeopleResponseDto> {

    private final UserWishPeopleMongoRepository repository;

    @Override
    public void persist(UserWishPeopleResponseDto dto, String userCode) throws JsonProcessingException {

        List<JsonNode> userWishPeople = dto.getResult().getUserWishPeople();

        if (userWishPeople == null) {
            return;
        }

        log.debug("size: {}",userWishPeople.size());

        for (int idx = 0; idx < userWishPeople.size(); idx++) {

            log.debug(
                    "userWishPeoples idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(userWishPeople.get(idx))
            );
            log.debug("===========================");

            UserWishPeopleDocument document = new UserWishPeopleDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(userWishPeople.get(idx)));
            repository.save(document);
        }

    }
}
