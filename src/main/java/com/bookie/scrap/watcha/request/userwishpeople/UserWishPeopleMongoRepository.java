package com.bookie.scrap.watcha.request.userwishpeople;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserWishPeopleMongoRepository extends MongoRepository<UserWishPeopleDocument, String> {
}

