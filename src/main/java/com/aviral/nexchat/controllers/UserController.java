package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.requests.ChangePasswordRequest;
import com.aviral.nexchat.entities.CustomUserDetails;
import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.services.UserService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = Constants.FRONTEND_URL)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is null");

        User user = userService.getUserByUserName(authentication.getName());

        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(
            @RequestBody User newUserData,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || userDetails.getUser() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User!");

        User user = userService.getUserByUserName(userDetails.getUser().getUserName());

        if (user == null)
            return ResponseEntity.notFound().build();

        User updatedUser = userService.updateUser(user, newUserData);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null || userDetails.getUser() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User!");

        User user = userService.getUserByUserName(userDetails.getUser().getUserName());

        if (user == null)
            return ResponseEntity.notFound().build();

        userService.deleteUserById(user.getId());

        return ResponseEntity.ok("User Deleted Successfully!");
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Room>> getUserChats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserChatsSorted(userDetails.getUser()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("q") String q) {
        return ResponseEntity.ok(userService.searchUser(q));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User!");

        if (!userService.comparePasswords(request.getCurrentPassword(), user.getPassword()))
            return ResponseEntity.badRequest().body("Current password is incorrect");

        userService.updatePassword(user, request.getNewPassword());

        return ResponseEntity.ok("Password Updated Successfully!");
    }
}
