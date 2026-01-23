package com.aviral.nexchat.services;

import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.repositories.RoomRepository;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    public String uploadFile(MultipartFile file, String roomId) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String fileName = roomId + "_" + fileId + "_" + file.getOriginalFilename();

        Path roomDir = Paths.get(Constants.BASE_UPLOAD_DOWNLOAD_DIR, "rooms", roomId);
        Files.createDirectories(roomDir);

        Path target = roomDir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

}
