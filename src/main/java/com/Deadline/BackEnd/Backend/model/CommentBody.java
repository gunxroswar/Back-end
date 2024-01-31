package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name="COMMENT")
public class CommentBody {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long commentID;

//    @NotNull(message = "postID should not be null")
//    @Column(nullable = false)
//    private Long postID;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    private PostBody post;

//    @NotNull(message = "commentOwerID should not be null")
//    @Column(nullable = false)
//    private Long commentOwerID;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "owerId")
    private User user;

    @OneToMany(mappedBy = "comment")
    private List<ReplyBody> replyBodies;


    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String detail;

//    @Column(nullable = false)
//    private Long like;

    @Column(nullable = false)
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean isVerify;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "postStatusId")
    private  PostStatus postStatus;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = false,updatable = false)
    private Date createAt;
    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;

}
