package com.bookie.scrap.watcha.request.userinfo;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserInfoMongoRepository extends MongoRepository<UserInfoDocument, String> {
}

