package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name="BOOKMARK")
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

//    @OneToOne
//    @JoinColumn(name = "uid")
//    private User user;

    @ManyToMany
    @JoinTable(
            name = "bookmark_post",
            joinColumns = @JoinColumn(name="bookmarkId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private List<PostBody> posts;
}
