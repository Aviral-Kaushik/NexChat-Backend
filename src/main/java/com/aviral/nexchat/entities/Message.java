package com.aviral.nexchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private String id;

    private String roomId;

    private String sender;

    // TEXT message → actual text
    // FILE message → optional caption
    private String content;

    private MessageType type;

    // Only present when type == FILE
    private FileMeta file;

    private LocalDateTime timestamp;
}
