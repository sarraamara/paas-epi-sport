package com.sport.usersapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/get-user/{userId}")
    public Optional<User> findAllById(@PathVariable("userId") String userId) {
        return userRepository.findById(userId);
    }
    @GetMapping("/users/get-user")
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
    @PostMapping("/users/del-user/{userId}")
    public void deleteById(@PathVariable("userId") String userId) {
        userRepository.deleteById(userId);
    }
}
