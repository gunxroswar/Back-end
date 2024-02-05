package com.Deadline.BackEnd.Backend.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POSTSTATUS")
public class PostStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postStatusID;
    @Column(nullable = false)
    private String postStatusName;
    @OneToMany(mappedBy = "statusOfPost")
    private List<Post> postBodies;
    @OneToMany(mappedBy = "statusOfComment")
    private List<Comment> commentBodies;
    @OneToMany(mappedBy = "statusOfReply")
    private List<Reply> replyBodies ;

}
