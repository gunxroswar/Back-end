package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.AuthResponseDto;
import com.Deadline.BackEnd.Backend.Dto.LoginDto;
import com.Deadline.BackEnd.Backend.Dto.SignUpDto;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.RoleRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.security.JWTAuthenticationFilter;
import com.Deadline.BackEnd.Backend.security.JWTGenerator;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    //@Autowired
    private AuthenticationManager authenticationManager;
    //@Autowired
    private UserRepository userRepository;
    //@Autowired
    private RoleRepository roleRepository;
    //@Autowired
    /*private PasswordEncoder passwordEncoder;*/
   // @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
       // this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/guest/jwtlogin")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){

        String sha256hex = Hashing.sha256()
                .hashString(loginDto.getPassword(), StandardCharsets.UTF_8)
                .toString();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        Hashing.sha256().hashString(loginDto.getPassword(), StandardCharsets.UTF_8).toString()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.GenerateToken(authentication);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/guest/jwtsignup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        String sha256hex;

        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByProfileName(signUpDto.getName())){
            return new ResponseEntity<>("This profile name is taken!", HttpStatus.BAD_REQUEST);
        }
        sha256hex = Hashing.sha256().hashString(signUpDto.getPassword(), StandardCharsets.UTF_8).toString();

        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(sha256hex);
        user.setProfileName(signUpDto.getName());

        userRepository.save(user);

        return new ResponseEntity<>("Signup new user complete", HttpStatus.OK);

    }

    @PostMapping("/pika")
    public String pika(){
        return "Hi";
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new JWTAuthenticationFilter();
//    }


}
