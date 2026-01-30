package com.aviral.nexchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String id;
    private String roomId;

    @DBRef(lazy = true)
    private List<Message> messages = new ArrayList<>();

    // Time Stamp for the last message so that we can query recent chats for users
    private LocalDateTime lastMessageAt;
}
