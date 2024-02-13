package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/hello")
    public  String hello()
    {
        return  "hello";
    }

    @PostMapping("/userx05")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/usertest")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
