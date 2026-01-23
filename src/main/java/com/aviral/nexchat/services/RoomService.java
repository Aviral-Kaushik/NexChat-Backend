package com.aviral.nexchat.services;

import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room getRoomById(String roomId) {
        return roomRepository.findByRoomId(roomId);
    }


}
