package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long commentId;


    @NotNull(message = "postID must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    private Post post;

    @NotNull(message = "owerID must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "owerId")
    private User user;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replyBodies;

//    @NotBlank(message = "topic must not be Blank")
//    @Size(max = 512,message = "topic is at most 512 characters.")
//    @Column(nullable = false,length = 512)
//    private String topic;

    @NotBlank(message = "detail must not be Blank")
    @Size(max = 512,message = "topic is at most 4096 characters.")
    @Column(nullable = false,length = 4096)
    private String detail;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean isVerify;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "postStatusId")
    private  PostStatus statusOfComment;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = false,updatable = false)
    private Date createAt;
    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;
    @ManyToMany
    @JoinTable(
            name = "like_comment",
            joinColumns = @JoinColumn(name = "commentId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> userLikeComment;

}
