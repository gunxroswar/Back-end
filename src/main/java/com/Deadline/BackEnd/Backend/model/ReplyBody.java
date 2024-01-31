package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@Table(name="REPLY")
public class ReplyBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLYID")
    private Long replyID;

//    @Column(name = "COMMENTID")
//    private Long commentID;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "commetId")
    private CommentBody comment ;
//    @Column(name = "REPLYOWER",nullable = false)
//    private Long UID;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "owerId")
    private User user;
    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String detail;
//    @Column(nullable = false)
//    private Long like ;
    @Column(nullable = false)
    private Boolean anonymous = false;
    @Column(nullable = false)
    private Boolean isVerify =  false;

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