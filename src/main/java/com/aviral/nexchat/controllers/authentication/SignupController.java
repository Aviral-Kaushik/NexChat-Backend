package com.aviral.nexchat.controllers.authentication;

import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping("signup")
    private ResponseEntity<?> signup(@RequestBody User user) {
        User userInDb = userService.getUserByUserName(user.getUserName());

        if (userInDb != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        userService.createUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
