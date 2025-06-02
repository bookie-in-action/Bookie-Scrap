package com.bookie.scrap.watcha.request.user.userwishbook;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserWishBookMongoRepository extends MongoRepository<UserWishBookDocument, String> {
}

