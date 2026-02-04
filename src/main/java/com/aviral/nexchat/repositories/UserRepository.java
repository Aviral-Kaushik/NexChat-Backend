package com.aviral.nexchat.repositories;

import com.aviral.nexchat.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserName(String username);

    void deleteByUserName(String username);

    List<User> findByUserNameStartingWithIgnoreCase(String username);

    User findByEmail(String email);

}
