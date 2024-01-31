package com.Deadline.BackEnd.Backend.Dto;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    //id for login
    private String username;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private String role;
}
