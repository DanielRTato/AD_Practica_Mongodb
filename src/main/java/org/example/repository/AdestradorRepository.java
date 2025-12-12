package org.example.repository;

import org.example.model.Adestrador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdestradorRepository extends MongoRepository<Adestrador, String> {
}
