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
@Table(name="POST")
public class PostBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTID")
    private Long postid;

    @Column(name = "POSTOWER",nullable = false)
    private Long uid;

    @Column(name = "TOPIC",nullable = false)
    private String topic;

    @Column(name = "DETAIL",nullable = false)
    private String detail;
    @Column(name = "LIKE",nullable = false)
    private Long like;
    @Column(name = "ANONYMOUS",nullable = false)
    private Boolean anonymous;
    @Column(name = "HASVERIFY",nullable = false)
    private Boolean hasverify;
    @Column(name = "POSTSTATUS",nullable = false)
    private Boolean poststatus;
    @CreationTimestamp
    private Date dateCreate;
    @UpdateTimestamp
    private Date lastUpdate;

}
