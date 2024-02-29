package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.UserDto;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.List;

public class AuthController {
    //handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    @GetMapping("/registor")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }



//    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
//
//        User existingUser = UserService.findUserByUsername(userDto.getUsername()) ;
//
//        //ถ้าอีเมลถูกใช้สมัครแล้ว reject
//        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
//            result.rejectValue("username", null,
//                    "There is already an account registered with the same username");
//        }
//
//        if(result.hasErrors()){
//            model.addAttribute("user", userDto);
//            return "/register";
//        }
//
//        UserService.saveUser(userDto);
//        return "redirect:/register?success";
//    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = UserService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
