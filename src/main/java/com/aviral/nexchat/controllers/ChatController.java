package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.Message;
import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.payload.MessageRequest;
import com.aviral.nexchat.services.RoomService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@CrossOrigin(Constants.FRONTEND_URL)
public class ChatController {

    @Autowired
    private RoomService roomService;

    // For sending and Receiving Messages
    @MessageMapping("/sendMessage/{roomId}") // Message to be sent on /app/sendMessage/roomId
    @SendTo("/topic/room/{roomId}") // Subscribe to this endpoint
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ) {
        Room room = roomService.getRoomById(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimestamp(LocalDateTime.now());

        if (room != null) {
            room.getMessages().add(message);
            roomService.saveRoom(room);
        } else {
            // Invalid Room
            throw new RuntimeException("Room not found!");
        }

        return message;
    }

}
