package com.aviral.nexchat.controllers.authentication;

import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.entities.requests.ResetPasswordRequest;
import com.aviral.nexchat.services.EmailService;
import com.aviral.nexchat.services.UserService;
import com.aviral.nexchat.utils.Constants;
import com.aviral.nexchat.utils.EmailTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RestController
@CrossOrigin(origins = Constants.FRONTEND_URL)
public class ForgotResetPassword {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userService.getUserByEmail(email);

        if (user == null)
            return ResponseEntity.badRequest().body("User not exists");

        String token = UUID.randomUUID().toString();

        user.setResetToken(passwordEncoder.encode(token));
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userService.saveUser(user);

        String resetLink = Constants.FRONTEND_URL + "/reset-password?token=" + token + "&email=" + email;

        String html = EmailTemplates.passwordResetEmail(
                user.getUserName(),
                resetLink
        );

        emailService.sendHtmlMail(
                user.getEmail(),
                "Reset your NexChat password",
                html
        );

        return ResponseEntity.ok("Reset Link shared on email id");
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        User user = userService.getUserByEmail(request.getEmail());

        if (user == null
        || user.getResetToken() == null
        || user.getResetTokenExpiry().isBefore(LocalDateTime.now())
        || !passwordEncoder.matches(request.getToken(), user.getResetToken())) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(request.getNewPassword())));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userService.saveUser(user);

        return ResponseEntity.ok("Password reset successfully!");
    }

}
