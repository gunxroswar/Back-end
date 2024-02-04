package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name ="statusId")
    private UserStatus userStatus;


    @OneToMany(mappedBy = "user")
    private Set<PostBody> posts;



    @ManyToMany
    @JoinTable(
            name = "bookmark",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private Set<PostBody> bookmarkPosts;



}




