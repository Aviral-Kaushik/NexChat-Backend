package com.aviral.nexchat.repositories;

import com.aviral.nexchat.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {

    // Get room using room id;
    Room findByRoomId(String roomId);
}
