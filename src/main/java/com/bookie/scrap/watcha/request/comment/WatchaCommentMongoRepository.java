package com.bookie.scrap.watcha.request.comment;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface WatchaCommentMongoRepository extends MongoRepository<WatchaCommentDocument, String> {
}

