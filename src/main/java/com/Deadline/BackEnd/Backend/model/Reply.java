package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="REPLY")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLYID")
    private Long replyId;

//    @Column(name = "COMMENTID")

    @NotNull(message = "commentID must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "commentId")
    private Comment comment ;
//    @Column(name = "REPLYOWER",nullable = false)

    @NotNull(message = "owerID must not be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "owerId")
    private User user;



    @NotBlank(message = "detail must not be Blank")
    @Size(max = 512,message = "topic is at most 4096 characters.")
    @Column(nullable = false,length = 4096)
    private String detail;

    @Column(nullable = false)
    private Long likeCount ;
    @Column(nullable = false)
    private Boolean anonymous = false;
    @Column(nullable = false)
    private Boolean isVerify =  false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "postStatusId")
    private  PostStatus statusOfReply;

    @CreationTimestamp
    @Column(name = "CreateAt",nullable = false,updatable = false)
    private Date createAt;
    @UpdateTimestamp
    @Column(name = "UpdateAt")
    private Date updateAt;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "like_reply",
            joinColumns = @JoinColumn(name = "replyId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> userLikeReply = new HashSet<>();
}
