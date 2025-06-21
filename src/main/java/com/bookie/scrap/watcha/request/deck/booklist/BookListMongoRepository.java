package com.bookie.scrap.watcha.request.deck.booklist;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookListMongoRepository extends MongoRepository<BookListDocument, String> {
}

