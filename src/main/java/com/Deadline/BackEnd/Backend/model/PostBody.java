package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name="POST")
public class PostBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long postid;


//    @Column(nullable = false)
//    private Long postOwerId;
    @ManyToOne
    @JoinColumn(name= "owerId")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<CommentBody> commentBodies;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String detail;

//    @Column(nullable = false)
//    private Long like;

    @Column(nullable = false)
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean hasVerify;

//    @Column(nullable = false)
//    private Long postStatus;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "postStatusId")
    private  PostStatus postStatus;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = false,updatable = false)
    private Date createAt;

    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;

    @ManyToMany(mappedBy = "posts")
    private List<BookMark> bookMarks;

}
