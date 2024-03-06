package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(nullable = true,length = 512)
    private String topic;

    @NotBlank(message = "detail must not be Blank")
    @Size(max = 512,message = "topic is at most 4096 characters.")
    @Column(nullable = true,length = 4096)
    private String detail;


    @Column(nullable = true)
    private Long likeCount= 0L;

    @Column(nullable = true)
    private Boolean anonymous=false;

    @Column(nullable = true)
    private Boolean hasVerify=false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "StatusId")
    private  PostStatus statusOfPost;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = true,updatable = false)
    private Date createAt;

    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;

    @ManyToMany(mappedBy = "bookmarkPosts")
    public Set<User> userBookmarks = new HashSet<>();

    @ManyToMany(mappedBy = "postWithTags")
    private Set<TagName> tagNames = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "like_post",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> userLikePost = new HashSet<>();


}
