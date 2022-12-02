package com.sport.usersapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/get-user/{userId}")
    public User findAllById(@PathVariable("userId") Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    @GetMapping("/")
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @PostMapping("/del-user/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userRepository.deleteByUserId(userId);
    }
    @PostMapping("/save-user/{userId}/{lastname}/{firstname}/{age}/{weight}/{height}")
    public void save(@PathVariable("userId") Long userid,
                     @PathVariable("lastname") String lastname,
                     @PathVariable("firstname") String firstname,
                     @PathVariable("age") int age,
                     @PathVariable("weight") int weight,
                     @PathVariable("height") int height) {
        User user = new User();
        user.setUserId(userid); user.setLastname(lastname); user.setAge(age); user.setWeight(weight); user.setFirstname(firstname); user.setWeight(weight); user.setHeight(height);
        userRepository.save(user);
    }
}
