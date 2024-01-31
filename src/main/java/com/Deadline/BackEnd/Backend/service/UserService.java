package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.Dto.UserDto;
import com.Deadline.BackEnd.Backend.model.User;

import java.util.List;

public interface UserService {

    static void saveUser(UserDto userDto) {
    }

    static User findUserByUsername(String Username) {
        return null;
    }

    static List<UserDto> findAllUsers(){
        return null;
    }
}
