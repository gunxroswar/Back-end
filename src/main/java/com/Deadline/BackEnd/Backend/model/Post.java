package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Table(name="POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotNull(message = "owerID must not be null")
    @ManyToOne
    @JoinColumn(name= "owerId")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentBodies;

    @NotBlank(message = "topic must not be Blank")
    @Size(max = 512,message = "topic is at most 512 characters.")
    @Column(nullable = false,length = 512)
    private String topic;

    @NotBlank(message = "detail must not be Blank")
    @Size(max = 512,message = "topic is at most 4096 characters.")
    @Column(nullable = false,length = 4096)
    private String detail;


    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Boolean anonymous=false;

    @Column(nullable = false)
    private Boolean hasVerify=false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "StatusId")
    private  PostStatus statusOfPost;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = false,updatable = false)
    private Date createAt;

    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;

    @ManyToMany(mappedBy = "bookmarkPosts")
    private Set<User> userBookmarks;

    @ManyToMany(mappedBy = "postWithTags")
    private Set<TagName> tagNames;

    @ManyToMany
    @JoinTable(
            name = "like_post",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> userLikePost;


}
