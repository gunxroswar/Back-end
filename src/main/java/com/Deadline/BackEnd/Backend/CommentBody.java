package com.Deadline.BackEnd.Backend;

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
@Table(name="COMMENT")
public class CommentBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENTID")
    private Long commentID;

    @Column(name = "POSTID")
    private Long postID;

    @Column(name = "COMMENTOWER",nullable = false)
    private Long UID;
    @Column(name = "TOPIC",nullable = false)
    private String topic;

    @Column(name = "DETAIL",nullable = false)
    private String detail;
    @Column(name = "LIKE",nullable = false)
    private Long like;
    @Column(name = "ANONYMOUS",nullable = false)
    private Boolean anonymous;
    @Column(name = "ISVERIFY",nullable = false)
    private Boolean isVerify;
    @Column(name = "POSTSTATUS",nullable = false)
    private Boolean postStatus;
    @CreationTimestamp
    private Date dateCreate;
    @UpdateTimestamp
    private Date lastUpdate;

}
