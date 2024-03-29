package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@Table(name="TAG")
public class TagName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long tagID;
    @Column(nullable = false)
    private String tagName;
    @ManyToMany
    @JoinTable(
            name = "tag_post",
            joinColumns = @JoinColumn(name = "tagId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private Set<Post> postWithTags = new HashSet<>();
}

