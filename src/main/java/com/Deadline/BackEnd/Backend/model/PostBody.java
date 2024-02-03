package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Table(name="POST")
public class PostBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long postid;

    @ManyToOne
    @JoinColumn(name= "owerId")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<CommentBody> commentBodies;

    @Column(nullable = false,length = 512)
    private String topic;

    @Column(nullable = false,length = 4096)
    private String detail;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean hasVerify;


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
