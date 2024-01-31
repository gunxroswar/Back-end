package com.Deadline.BackEnd.Backend.model;

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

    @Column(name = "IMAGEDIR")
    private String imageDir;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name ="ROLE",nullable = false)
    private String roles ;

    @Column(name = "PROFILENAME",nullable = false)
    private String profileName;

    @Column(name = "USERNAME",nullable = false)
    private String username;

    @Column(name ="PASSWORD", nullable = false)
    private String password;

    @Column(name = "StatusID",nullable = false)
    private String status;

}




