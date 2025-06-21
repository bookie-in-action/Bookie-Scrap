package com.bookie.scrap.watcha.request.book.booktodecks;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookToDecksMongoRepository extends MongoRepository<BookToDecksDocument, String> {
}

