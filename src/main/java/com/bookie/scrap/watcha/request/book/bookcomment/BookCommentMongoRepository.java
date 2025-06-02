package com.bookie.scrap.watcha.request.book.bookcomment;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookCommentMongoRepository extends MongoRepository<BookCommentDocument, String> {
}

