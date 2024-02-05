package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.Dto.UserDto;
import com.Deadline.BackEnd.Backend.model.Role;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.RoleRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;

//import com.Deadline.BackEnd.Backend.repository.RoleRepository;
//import com.Deadline.BackEnd.Backend.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
//
//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;
//
//    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }



//    public void saveUser(UserDto userDto){
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        // encrypt the password using spring security
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//        Role role = roleRepository.findByName("ADMIN");
//        if(role == null){
//            role = checkRoleExist();
//        }
//        user.setRoles(List.of(role));
//        userRepository.save(user);
//
//    }



//    public List<UserDto> findAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map((user) -> mapToUserDto(user))
//                .collect(Collectors.toList());
//    }

//    private UserDto mapToUserDto(User user){
//        UserDto userDto = new UserDto();
//        String str = user.getUsername();
//        userDto.setUsername(str);
//
//        return userDto;
//    }

//    private Role checkRoleExist(){
//        Role role = new Role();
//        role.setName("ADMIN");
//        return roleRepository.save(role);
//    }

}
