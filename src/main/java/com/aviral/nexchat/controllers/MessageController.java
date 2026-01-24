package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.Message;
import com.aviral.nexchat.services.MessageService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@CrossOrigin(Constants.FRONTEND_URL)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageById(
            @PathVariable String messageId
    ) {
        Message message = messageService.getMessageById(messageId);

        if (message == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<?> updateMessage(
            @PathVariable String messageId,
            @RequestBody Message message
    ) {
        Message savedMessage = messageService.getMessageById(messageId);

        if (message == null)
            return ResponseEntity.notFound().build();

        Message updatedMessage = messageService.updateMessage(savedMessage, message);

        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable String messageId
    ) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message Deleted");
    }
}
