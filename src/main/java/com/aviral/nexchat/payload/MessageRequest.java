package com.aviral.nexchat.payload;

import com.aviral.nexchat.entities.FileMeta;
import com.aviral.nexchat.entities.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String content;
    private String sender;
    private String roomId;

    // FILE or TEXT
    private MessageType type;

    // Only present when type == FILE
    private FileMeta file;
}
