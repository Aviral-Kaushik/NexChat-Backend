package com.aviral.nexchat.services;

import com.aviral.nexchat.entities.Message;
import com.aviral.nexchat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message getMessageById(String messageId) {
        return messageRepository.findByMessageId(messageId);
    }

    public Message updateMessage(Message savedMessage, Message newMessage) {
        Message message = new Message();

        message.setId(savedMessage.getId());
        message.setRoomId(savedMessage.getRoomId());
        message.setSender(newMessage.getSender());
        message.setContent(newMessage.getContent());
        message.setType(newMessage.getType());
        message.setFile(newMessage.getFile());
        message.setTimestamp(savedMessage.getTimestamp());

        return saveMessage(message);
    }

    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
    }
}
