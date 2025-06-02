package com.bookie.scrap.watcha.request.user.userbookrating;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserBookRatingMongoRepository extends MongoRepository<UserBookRatingDocument, String> {
}

