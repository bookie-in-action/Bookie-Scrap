package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserInfoPersister implements WatchaPersistor<UserInfoResponseDto> {

    private final UserInfoMongoRepository repository;

    @Override
    public int persist(UserInfoResponseDto dto, String userCode) {

        JsonNode userInfo = dto.getUserInfo();

        if (userInfo == null || userInfo.isEmpty()) {
            return 0;
        }

        try {
            log.info("UserMeta size: 1");

            UserInfoDocument document = new UserInfoDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(userInfo));
            repository.save(document);

            log.info("userCode: {} userInfo saved", userCode);
            log.debug(JsonUtil.toPrettyJson(userInfo));
            log.debug("===========================");

            return 1;
        } catch (JsonProcessingException e) {
            log.error("json 파싱 실패");
            return 0;
        }
    }
}
