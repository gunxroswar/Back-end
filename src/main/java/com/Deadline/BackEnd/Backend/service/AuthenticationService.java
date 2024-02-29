package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.model.AuthenticationResponse;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.google.common.hash.Hashing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final String SALT = "X9+_t?JF9z-x8fj";

private final AuthenticationManager authenticationManager;
    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<String> register(User request){
        boolean password = true;
        String sha256hex;
        Boolean username = repository.findByUsername(request.getUsername()).isEmpty();
        Boolean profileName= repository.findByProfileName(request.getProfileName()).isEmpty();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setProfileName(request.getProfileName());
        try{
            if(request.getPassword().length()<8){
                password = false;
            }
            if(username && profileName && password){
                sha256hex = Hashing.sha256()
                        .hashString(SALT+request.getPassword(), StandardCharsets.UTF_8)
                        .toString();
                user.setPassword(sha256hex);
//                user.setRole(1);
                user = repository.save(user);
            }else throw new RuntimeException();
        }catch (Exception e){
            return ResponseEntity.badRequest().body("{username: "+username+"," +
                    " usernameDetail: "+ (username?"OK":"Username is duplicated.") +"," +
                    " password: "+password+"," +
                    " passwordDetail: "+(password?"OK":"Password must be more than 8 character.")+" }");
        }

        return new ResponseEntity<String>("Success", HttpStatus.CREATED);



//        String token = jwtService.generateToken(user);
//        return new AuthenticationResponse(token);
    }
    public ResponseEntity<AuthenticationResponse> authenticate(User request){

        List<User> user= repository.findByUsername(request.getUsername());
        String sha256hex = Hashing.sha256()
                .hashString(SALT+request.getPassword(), StandardCharsets.UTF_8)
                .toString();

        if(user.isEmpty()){
            return new ResponseEntity<AuthenticationResponse>((AuthenticationResponse) null, HttpStatus.BAD_REQUEST);
        }else if(sha256hex.equals(user.getFirst().getPassword())){
            String token = jwtService.generateToken(user.getFirst());
            return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(token), HttpStatus.OK);
        }else{
            return new ResponseEntity<AuthenticationResponse>((AuthenticationResponse) null, HttpStatus.BAD_REQUEST);
        }

    }
}