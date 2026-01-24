package com.aviral.nexchat.repositories;

import com.aviral.nexchat.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
