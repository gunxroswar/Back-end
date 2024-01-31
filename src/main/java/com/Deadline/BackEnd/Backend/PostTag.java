package com.Deadline.BackEnd.Backend;

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
    @Column(name = "ID")
    private Long id;
    @Column(name = "POSTID")
    private Long postID;
    @Column(name = "TAGID")
    private Long tagID;
}
