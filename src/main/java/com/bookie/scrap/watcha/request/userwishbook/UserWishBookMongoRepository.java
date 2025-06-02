package com.bookie.scrap.watcha.request.userwishbook;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserWishBookMongoRepository extends MongoRepository<UserWishBookDocument, String> {
}

