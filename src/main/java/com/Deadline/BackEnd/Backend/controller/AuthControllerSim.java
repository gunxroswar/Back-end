package com.Deadline.BackEnd.Backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.Deadline.BackEnd.Backend.userLogin.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
public class AuthControllerSim {
    private List<User> user = new ArrayList<>();

    private final AtomicLong counter = new AtomicLong();
    public AuthControllerSim(){

    }

    @PostMapping("/guests/register")
    public String register(@RequestBody User u){
        user.add(new User(counter.getAndIncrement(),u.getDisplay_name(),u.getUsername(),u.getPassword()));
        return "Success";
    }

    @PostMapping("/guest/login")
    public boolean login(@RequestBody User u){
        long id = -1;
        for (int i = 0 ;i<user.size();i++){
            if (u.getUsername().equals(user.get(i).getUsername())){
                id = i;
            }
        }
        if(u.getPassword().equals(user.get((int) id).getPassword())){
            return true;
        }
        return false;
    }

}
