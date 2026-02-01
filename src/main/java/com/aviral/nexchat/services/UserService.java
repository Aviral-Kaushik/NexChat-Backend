package com.aviral.nexchat.services;

import com.aviral.nexchat.entities.Room;
import com.aviral.nexchat.entities.User;
import com.aviral.nexchat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        oldUserData.setEmail(updatedUserData.getEmail());

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

    public void addRoomToUser(User user, Room room) {
        if (user == null) return;

        if (user.getChats() == null) {
            user.setChats(new ArrayList<>());
        }

        boolean alreadyJoined = user.getChats()
                .stream()
                .anyMatch(r -> r.getRoomId().equals(room.getRoomId()));

        if (!alreadyJoined) {
            user.getChats().add(room);
            userRepository.save(user);
        }
    }

    public List<Room> getUserChatsSorted(User user) {
        if (user == null || user.getChats() == null) return new ArrayList<>();

        return user.getChats().stream()
                .sorted((r1, r2) -> {
                    LocalDateTime t1 = r1.getLastMessageAt();
                    LocalDateTime t2 = r2.getLastMessageAt();

                    if (t1 == null && t2 == null) return 0;
                    if (t1 == null) return 1;  // null goes last
                    if (t2 == null) return -1; // null goes last

                    return t2.compareTo(t1); // descending order
                })
                .toList();
    }

    public List<User> searchUser(String username) {
        return userRepository
                .findByUserNameStartingWithIgnoreCase(username)
                .stream()
                .limit(10)
                .toList();
    }
}
