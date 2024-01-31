package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name="USERSTATUS")
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusID;
    @Column(nullable = false)
    private String statusName;
    @OneToMany(mappedBy = "userStatus")
    private List<User> users;

    }
