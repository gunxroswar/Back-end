package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.Dto.UserDto;
import com.Deadline.BackEnd.Backend.exception.model.Role;
import com.Deadline.BackEnd.Backend.exception.model.User;
import com.Deadline.BackEnd.Backend.repository.RoleRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void saveUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(userDto.getRole());
        userRepository.save(user);


    }


    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String str = user.getName();
        userDto.setName(str);

        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ADMIN");
        return roleRepository.save(role);
    }

}
