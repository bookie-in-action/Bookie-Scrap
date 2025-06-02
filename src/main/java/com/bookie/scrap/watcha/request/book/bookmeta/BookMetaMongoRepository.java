package com.bookie.scrap.watcha.request.book.bookmeta;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookMetaMongoRepository extends MongoRepository<BookMetaDocument, String> {
}

