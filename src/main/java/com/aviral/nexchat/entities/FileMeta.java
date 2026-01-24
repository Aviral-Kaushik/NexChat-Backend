package com.aviral.nexchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileMeta {
    // Original filename shown to user
    private String originalName;

    // Stored filename on disk or storage
    private String storedName;

    // Public or backend download URL
    private String downloadUrl;

    private String mimeType;

    private long size;
}
