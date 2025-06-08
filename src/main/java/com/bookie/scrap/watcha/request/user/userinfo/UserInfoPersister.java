package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoPersister implements WatchaPersistFactory<UserInfoResponseDto> {

    private final UserInfoMongoRepository repository;

    @Override
    public int persist(UserInfoResponseDto dto, String userCode) throws JsonProcessingException {

        JsonNode userInfo = dto.getUserInfo();

        if (userInfo == null) {
            return 0;
        }

        log.debug(JsonUtil.toPrettyJson(userInfo));
        log.debug("===========================");

        UserInfoDocument document = new UserInfoDocument();
        document.setUserCode(userCode);
        document.setRawJson(JsonUtil.toMap(userInfo));
        repository.save(document);

        return 1;
    }
}
