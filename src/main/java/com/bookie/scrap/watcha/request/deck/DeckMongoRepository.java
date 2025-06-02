package com.bookie.scrap.watcha.request.deck;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface DeckMongoRepository extends MongoRepository<DeckDocument, String> {
}

