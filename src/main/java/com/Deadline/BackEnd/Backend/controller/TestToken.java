package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestToken {
    public JwtService jwt = new JwtService();
    @GetMapping("/v1/test/token")
    public String yourEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the bearer token from the Authorization header
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        String u=jwt.extractUID(bearerToken);
        // Now you can use the bearerToken as needed
        // ...

        return "Token received successfully : "+bearerToken +"\n" + u;
    }
}