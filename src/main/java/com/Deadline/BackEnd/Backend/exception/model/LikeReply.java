package com.Deadline.BackEnd.Backend.exception.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="LIKEREPLY")

public class LikeReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REPLYID")
    private Long REPLYID;
    @Column(name="USERID")
    private  Long UID;
}
