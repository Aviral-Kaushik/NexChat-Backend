package com.aviral.nexchat.controllers.authentication;

import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.authentication.JWTService;
import com.aviral.nexchat.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUserName(), user.getPassword()));

            UserDetails userDetails = userService.loadUserByUsername(user.getUserName());

            String jwtToken = jwtService.generateToken(userDetails);

            return new ResponseEntity<>(Map.of("token", jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
