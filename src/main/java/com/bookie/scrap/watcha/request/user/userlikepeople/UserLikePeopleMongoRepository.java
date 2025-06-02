package com.bookie.scrap.watcha.request.user.userlikepeople;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserLikePeopleMongoRepository extends MongoRepository<UserLikePeopleDocument, String> {
}

