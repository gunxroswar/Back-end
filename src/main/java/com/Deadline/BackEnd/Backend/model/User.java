package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

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


    private String imageDir = "image.png";

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private List<Role> roles = new ArrayList<>();

    @NotBlank(message= "ProfileName must not be blank")
    @Size(min = 2 ,message = "ProfileName must be more than 2 character")
    @Column(nullable = false,unique = true)
    private String profileName;

    @NotBlank(message= "Username must not be blank")
    @Size(min = 2 ,message = "Username must be more than 2 character")
    @Column(nullable = false,unique = true)
    private String username;

    @NotBlank(message= "Password must not be blank")
    @Size(min = 8 ,message = "Password must be more than 8 character")
    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)

    @ManyToOne
    @JoinColumn(name ="statusId")
    private UserStatus userStatus;


    @OneToMany(mappedBy = "user")
    private Set<Post> posts;


    @ManyToMany
    @JoinTable(
            name = "bookmark",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private Set<Post> bookmarkPosts;



}




