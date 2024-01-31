package com.Deadline.BackEnd.Backend;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UID")
    private Long UID;
    @Column(name = "NAME",nullable = false)
    private String name;
    @Column(name ="ROLE",nullable = false)
    private String role ;
    @Column(name = "PROFILENAME",nullable = false)
    private String profileName;
    @Column(name = "USERNAME",nullable = false)
    private String username;
    @Column(name = "StatusID",nullable = false)
    private String status;

}




