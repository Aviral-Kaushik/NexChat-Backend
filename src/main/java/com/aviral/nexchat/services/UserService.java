package com.aviral.nexchat.services;

import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        try {
            user.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
            user.setRoles(List.of("USER"));
            user.setCreatedAt(LocalDateTime.now());
            saveUser(user);
        } catch (Exception e) {
            System.out.println("Exception while creating user: " + e.getMessage());
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public User updateUser(User oldUserData, User updatedUserData) {
        oldUserData.setUserName(updatedUserData.getUserName());
        oldUserData.setPassword(Objects.requireNonNull(passwordEncoder.encode(updatedUserData.getPassword())));

        userRepository.save(oldUserData);

        return oldUserData;
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public User deleteUserByUsername(String username) {
        User user = getUserByUserName(username);
        if (user == null)
            return null;

        userRepository.deleteByUserName(username);
        return user;
    }
}
