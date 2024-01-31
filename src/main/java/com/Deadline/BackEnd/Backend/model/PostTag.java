package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
@Getter
@Setter
@Table(name="POSTTAG")
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(nullable = false)
    private Long postID;
    @Column(nullable = false)
    private Long tagID;
}
