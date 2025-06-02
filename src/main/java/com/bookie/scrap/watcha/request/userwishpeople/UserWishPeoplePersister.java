package com.bookie.scrap.watcha.request.userwishpeople;

import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.bookie.scrap.watcha.request.bookcomment.BookCommentDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserWishPeoplePersister implements WatchaPersistFactory<UserWishPeopleResponseDto> {

    private final ObjectMapper mapper;
    private final UserWishPeopleMongoRepository repository;

    @Override
    public void persist(UserWishPeopleResponseDto dto, String userCode) throws JsonProcessingException {

        List<Object> userWishPeople = dto.getResult().getUserWishPeople();

        log.debug("size: {}",userWishPeople.size());

        for (int idx = 0; idx < userWishPeople.size(); idx++) {

            log.debug(
                    "comment idx: {}, value: {}",
                    idx,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userWishPeople.get(idx))
            );
            log.debug("===========================");

            UserWishPeopleDocument document = new UserWishPeopleDocument();
            document.setUserCode(userCode);
            document.setRawJson(userWishPeople.get(idx).toString());
            repository.save(document);
        }

    }
}
