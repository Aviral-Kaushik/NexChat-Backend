package com.aviral.nexchat.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String id;

    @NonNull
    @Indexed
    private String userName;

    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    // List of Rooms chats of a user to show him recent chats
    @DBRef(lazy = true)
    private List<Room> chats;

    private List<String> roles;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private LocalDateTime createdAt;
}
