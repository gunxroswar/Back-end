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
@Table(name="USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UID")
    private Long uid;

    @Column(nullable = true)
    private String imageDir;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role ;

    @Column(nullable = false)
    private String profileName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)
//    private String status;
    @ManyToOne
    @JoinColumn(name ="statusId")
    private UserStatus userStatus;


    @OneToMany(mappedBy = "user")
    private List<PostBody> postBodies;

//    @OneToOne
//    @JoinColumn(name="bookmarkId")
//    private BookMark bookmark;



}




