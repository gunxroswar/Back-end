package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//import com.Deadline.BackEnd.Backend.userLogin.User;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
public class AuthControllerSim {


    private final AtomicLong counter = new AtomicLong();
    @Autowired
    private UserRepository userRepository;

    Statement stmt = null;

    @PostMapping("/guests55/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<User> getUername(@RequestBody User u)
    {
        return userRepository.findByUsername(u.getUsername());
    }




    @PostMapping("/guests/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> register(@RequestBody @Valid User u, BindingResult bindingResult){
        boolean password = true;
        String sha256hex;
        Boolean username = !userRepository.findByUsername(u.getUsername()).isEmpty();
        Boolean profileName= !userRepository.findByProfileName(u.getProfileName()).isEmpty();

        try{
//            if(bindingResult.hasErrors()){
//                return ;
//            }

            if(u.getPassword().length()<8){
                password = false;
            }
            if(username && profileName && password){
                sha256hex = Hashing.sha256()
                        .hashString(u.getPassword(), StandardCharsets.UTF_8)
                        .toString();
                u.setPassword(sha256hex);
                userRepository.save(u);
            }else throw new RuntimeException();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{username: "+username+"," +
                    " usernameDetail: "+ (username?"OK":"Username is duplicated.") +"," +
                    " password: "+password+"," +
                    " passwordDetail: "+(password?"OK":"Password must be more than 8 character.")+" }");
        }
        return new ResponseEntity<String>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/guests/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> login(@RequestBody User u){
        List<User> user=userRepository.findByUsername(u.getUsername());
        String sha256hex = Hashing.sha256()
                .hashString(u.getPassword(), StandardCharsets.UTF_8)
                .toString();
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("Incorrect");
        }else if(sha256hex.equals(user.get(0).getPassword())){
            return new ResponseEntity("Success", HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().body("Incorrect");
        }
    }




}
