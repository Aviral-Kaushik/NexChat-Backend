package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.*;
import com.aviral.nexchat.services.RoomService;
import com.aviral.nexchat.services.UserService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = Constants.FRONTEND_URL)
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    // Create Room
    @PostMapping
    public ResponseEntity<?> createRoom(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        String roomId = payload.get("roomId");

        if (roomId == null || roomId.isBlank()) {
            return ResponseEntity.badRequest().body("roomId is required");
        }

        if (roomService.getRoomById(roomId) != null) {
            // Room already created with room id
            return ResponseEntity.badRequest().body("Room Already Exits");
        }

        // Create Room
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomService.saveRoom(room);

        // Add Room to the user rooms
        userService.addRoomToUser(userDetails.getUser(), room);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    // Get Room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(
            @PathVariable String roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found!");
        }

        // Add Room to user's room is not exists
        userService.addRoomToUser(userDetails.getUser(), room);

        return ResponseEntity.ok().body(room);
    }

    @PostMapping("one-to-one")
    public ResponseEntity<?> createOneToOneRoom(
            @RequestParam("username") String username,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || userDetails.getUser() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User!");

        if (username == null || username.isBlank())
            return ResponseEntity.badRequest().body("Invalid username for creating room");

        String currentUsername = userDetails.getUsername();

        if (currentUsername.equals(username))
            return ResponseEntity.badRequest().body("Cannot create room with same user");

        User otherUser = userService.getUserByUserName(username);
        if (otherUser == null)
            return ResponseEntity.badRequest().body("Invalid user for creating room");

        // Deterministic roomId
        String roomId = Stream.of(currentUsername, username)
                .sorted()
                .collect(Collectors.joining("-"));

        Room room = roomService.getRoomById(roomId);

        if (room == null) {
            room = new Room();
            room.setRoomId(roomId);
            room = roomService.saveRoom(room);
        }

        userService.addRoomToUser(userDetails.getUser(), room);
        userService.addRoomToUser(otherUser, room);

        return ResponseEntity.ok(room);
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

    @PostMapping("/upload/{roomId}")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable String roomId
    ) throws IOException {
        Room room = roomService.getRoomById(roomId);

        if (room == null) {
            return ResponseEntity.badRequest().body("Invalid Room Id!");
        }

        String fileName = roomService.uploadFile(file, roomId);

        return ResponseEntity.ok(new FileUploadResponse(
                fileName,
                file.getContentType(),
                file.getSize(),
                "rooms/download/" + roomId + "/" + fileName
        ));
    }

    @GetMapping("/download/{roomId}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String roomId,
            @PathVariable String fileName
    ) throws IOException {

        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        Path path = Paths.get(Constants.BASE_UPLOAD_DOWNLOAD_DIR, "rooms/" + roomId, fileName);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        Files.probeContentType(path)
                ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);

    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable String roomId
    ) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.notFound().build();
    }
}
