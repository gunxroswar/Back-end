package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.AuthenticationResponse;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.AuthenticationService;
import com.Deadline.BackEnd.Backend.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;
    public JwtService jwt = new JwtService();

    private final UserRepository repository;
    public AuthenticationController(AuthenticationService authService, UserRepository repository) {
        this.authService = authService;
        this.repository = repository;
    }

    @PostMapping("/guests/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> register(
            @RequestBody User request
            ){
        return authService.register(request);
    }
    @PostMapping("/guests/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request){
            return authService.authenticate(request);

    }

    @GetMapping("/testau")
    public ResponseEntity<String> demo(Model model){
        return ResponseEntity.ok("test pass");
    }

    @PostMapping("/guests/edit")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> editProfile(@RequestBody User request,@RequestHeader("Authorization") String authorizationHeader ){
        return authService.editProfile(request,authorizationHeader);
    }
    @GetMapping("/v1/guests/isAuth")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> isAuth(@RequestHeader("Authorization") String authorizationHeader){
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        String u=jwt.extractUID(bearerToken);
        User user= repository.findById(Long.parseLong(u)).orElseThrow(()-> new UserNotFoundException(Long.parseLong(u)));
        String profileName = user.getProfileName();

        return new ResponseEntity<>("{\"profileName\":\""+profileName+"\"}", HttpStatus.OK);
    }

}
