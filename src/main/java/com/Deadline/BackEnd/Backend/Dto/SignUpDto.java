package com.Deadline.BackEnd.Backend.Dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SignUpDto {

    private String username;

    private String password;

    private String name;
}
