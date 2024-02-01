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
@Table(name="POSTSTATUS")
public class PostStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postStatusID;
    @Column(nullable = false)
    private String postStatusName;
    @OneToMany(mappedBy = "statusOfPost")
    private List<PostBody> postBodies;
    @OneToMany(mappedBy = "statusOfComment")
    private List<CommentBody> commentBodies;
    @OneToMany(mappedBy = "statusOfReply")
    private List<ReplyBody> replyBodies ;

}
