package com.aviral.nexchat.controllers;

import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.services.UserService;
import com.aviral.nexchat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(Constants.FRONTEND_URL)
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

}
