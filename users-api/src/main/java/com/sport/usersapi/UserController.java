package com.sport.usersapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/get-user/{userId}")
    public User findAllById(@PathVariable("userId") Long userId) {
        User user = userRepository.findUserByUserId(userId);
        System.out.println(user);
        return user;
    }
    @GetMapping("/users/get-users")
    public Iterable<User> findAll() {
        Iterable<User> users = userRepository.findAll();
        System.out.println(users);
        return users;
    }
    @PostMapping("/users/del-user/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userRepository.deleteByUserId(userId);
    }
}
