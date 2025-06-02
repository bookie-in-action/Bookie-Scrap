package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoPersister implements WatchaPersistFactory<UserInfoResponseDto> {

    private final ObjectMapper mapper;
    private final UserInfoMongoRepository repository;

    @Override
    public void persist(UserInfoResponseDto dto, String userCode) throws JsonProcessingException {

        Object userInfo = dto.getUserInfo();

        log.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo));
        log.debug("===========================");

        UserInfoDocument document = new UserInfoDocument();
        document.setUserCode(userCode);
        document.setRawJson(userInfo.toString());
        repository.save(document);

    }
}
