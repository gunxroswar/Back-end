package com.Deadline.BackEnd.Backend.exception.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="LIKECOMMENT")

public class LikeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "COMMENTID")
    private Long commentID;
    @Column(name="USERID")
    private  Long UID;
}
