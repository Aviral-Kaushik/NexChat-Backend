package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.Message;
import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.services.RoomService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(Constants.FRONTEND_URL)
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Create Room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {
        if (roomService.getRoomById(roomId) != null) {
            // Room already created with room id
            return ResponseEntity.badRequest().body("Room Already Exits");
        }

        // Create Room
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomService.saveRoom(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    // Get Room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found!");
        }

        return ResponseEntity.ok().body(room);
    }

    // Get Messages of the Room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ) {
        Room room = roomService.getRoomById(roomId);

        if (room == null) {
            return ResponseEntity.badRequest().body("Invalid Room Id!");
        }

        // Pagination
        List<Message> messages = room.getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        List<Message> paginatedMessages = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessages);
    }
}
