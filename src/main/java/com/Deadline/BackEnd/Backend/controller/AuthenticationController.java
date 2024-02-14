package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.AuthenticationResponse;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
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

}
