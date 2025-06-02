package com.bookie.scrap.watcha.request.user.userwishbook;

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
public class UserWishBookPersister implements WatchaPersistFactory<UserWishBookResponseDto> {

    private final ObjectMapper mapper;
    private final UserWishBookMongoRepository repository;

    @Override
    public void persist(UserWishBookResponseDto dto, String userCode) throws JsonProcessingException {

        List<JsonNode> userWishBooks = dto.getResult().getUserWishBooks();

        if (userWishBooks == null) {
            return;
        }

        log.debug("size: {}",userWishBooks.size());

        for (int idx = 0; idx < userWishBooks.size(); idx++) {

            log.debug(
                    "userWishBook idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(userWishBooks.get(idx))
            );
            log.debug("===========================");

            UserWishBookDocument document = new UserWishBookDocument();
            document.setUserCode(userCode);
            document.setRawJson(JsonUtil.toMap(userWishBooks.get(idx)));
            repository.save(document);
        }

    }
}
