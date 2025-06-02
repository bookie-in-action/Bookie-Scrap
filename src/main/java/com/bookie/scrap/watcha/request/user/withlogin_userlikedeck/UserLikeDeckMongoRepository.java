package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserLikeDeckMongoRepository extends MongoRepository<UserLikeDeckDocument, String> {
}

