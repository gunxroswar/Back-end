package com.Deadline.BackEnd.Backend.exception.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="BOOKMARK")
public class BookMarks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARKID")
    private Long bookmarkID;
    @Column(name = "USERID")
    private Long UID;
    @Column(name = "POSTID")
    private Long postID;
}
