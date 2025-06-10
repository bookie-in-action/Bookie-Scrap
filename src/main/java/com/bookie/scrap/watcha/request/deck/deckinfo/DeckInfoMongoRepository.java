package com.bookie.scrap.watcha.request.deck.deckinfo;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface DeckInfoMongoRepository extends MongoRepository<DeckInfoDocument, String> {
}

