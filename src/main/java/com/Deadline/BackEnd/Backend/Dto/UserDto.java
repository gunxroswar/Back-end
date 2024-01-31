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

    private Long UID;
    @NotEmpty
    private String name;

    private String password;

    private String role;
}
