package com.aviral.nexchat.controllers;

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
    public ResponseEntity<?> updateUser(@RequestBody User newUserData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByUserName(authentication.getName());

        if (user == null)
            return ResponseEntity.notFound().build();

        User updatedUser = userService.updateUser(user, newUserData);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByUserName(authentication.getName());

        if (user == null)
            return ResponseEntity.notFound().build();

        userService.deleteUserById(user.getId());

        return ResponseEntity.ok("User Deleted Successfully!");
    }

    @GetMapping("greet")
    public ResponseEntity<?> greet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByUserName(authentication.getName());

        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok("Hi!, " + user.getUserName());
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Room>> getUserCats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserChatsSorted(userDetails.getUser()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("q") String q) {
        return ResponseEntity.ok(userService.searchUser(q));
    }

}
