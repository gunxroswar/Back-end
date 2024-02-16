package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.AuthResponseDto;
import com.Deadline.BackEnd.Backend.Dto.LoginDto;
import com.Deadline.BackEnd.Backend.Dto.SignUpDto;
import com.Deadline.BackEnd.Backend.model.Cookie;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.CookieRepository;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;
    // @Autowired
    private CookieRepository cookieRepository;
   // @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, CookieRepository cookieRepository, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/guest/jwtlogin")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String sha256hex = Hashing.sha256()
                .hashString(loginDto.getPassword(), StandardCharsets.UTF_8)
                .toString();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        sha256hex.toString()
                ));

        String token = "Ok"; /*jwtGenerator.GenerateToken(authentication);*/
        Optional<User> user = userRepository.findByUsername(loginDto.getUsername());

        if (user.isEmpty()) {
            return new ResponseEntity<>("You might not be sign up yet. ", HttpStatus.BAD_REQUEST);
        } else if ((sha256hex.equals(user.get().getPassword()))) {
            String cookieHash = jwtGenerator.GenerateToken(authentication);
            Optional<Cookie> cookie = cookieRepository.findByCookie(cookieHash);
            if(cookie.isEmpty()){
                Cookie newCookie = new Cookie();
                newCookie.setUser(userRepository.findByUsername(loginDto.getUsername()).get());
                newCookie.setCookie(cookieHash);
//                newCookie.setCookieId(
//                        (cookieRepository.findCookieWithMaxId()
//                        .map(Cookie::getCookieId) // Extract the price from the Product
//                        .orElse(0))+1
//                        );
                newCookie.setCookieId(cookieRepository.findMaxId()+1);
                newCookie.setUpdateAt(new Date());
                cookieRepository.save(newCookie);
            }
            else{
                cookie.get().setUpdateAt(new Date());
                cookieRepository.save(cookie.get());
            }

            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body
                    ("Incorrect " + loginDto.getUsername() + " " + loginDto.getPassword() + " " + sha256hex);
        }

}


    @PostMapping("/guest/jwtsignup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){

        String sha256hex = Hashing.sha256()
                .hashString(signUpDto.getPassword(), StandardCharsets.UTF_8)
                .toString();

        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByProfileName(signUpDto.getName())){
            return new ResponseEntity<>("This profile name is taken!", HttpStatus.BAD_REQUEST);
        }
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
