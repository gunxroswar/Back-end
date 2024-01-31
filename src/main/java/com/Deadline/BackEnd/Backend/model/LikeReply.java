package com.Deadline.BackEnd.Backend.model;

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

    private Long id;
    @Column(nullable = false)
    private Long replyID;
    @Column(nullable = false)
    private  Long uid;
}
