package com.Deadline.BackEnd.Backend.exception.model;

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

    @Column(name = "COMMENTID")
    private Long commentID;

    @Column(name = "REPLYOWER",nullable = false)
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
