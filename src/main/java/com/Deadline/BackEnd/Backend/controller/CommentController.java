package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.ReplyRepository;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.dialect.SybaseASEDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReplyRepository replyRepository;

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<Comment>> getCommentInPost(@PathVariable(name = "id") Long postId )
    {
        List<Comment> comments;
        try
        {
            Optional<Post> postOpt = postRepository.findById(postId);
            comments= commentRepository.findByPost(postOpt.orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(comments);
    }
    @PostMapping("/comments")
    public  ResponseEntity<String> insertComments(@RequestBody @Valid Comment comment)
    {
        Optional<Post> postOpt = postRepository.findById(comment.getPost().getPostId());
        if(postOpt.isEmpty())
        {
            return ResponseEntity.badRequest().body("Post is not found");
        }
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("");
        }
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/comments")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getComment(@RequestParam("commentId") Long id){
        Optional<Comment> search = commentRepository.findById(id);
        StringBuilder sendBack = new StringBuilder();
        if(search.isEmpty()) sendBack.append("[]");
        //comment_id as 'CommentID', user.profile_name as 'displayName', like_count as 'LikeAmount', is_verify as 'hasVerify', 0 as 'replyAmount', create_at as 'CreateDate', detail
        else{
            Long replyCount = replyRepository.countByComment(search.get());
            sendBack.append("{");
            sendBack.append("\"CommentID\":\"").append(search.get().getCommentId()).append("\",");
            //sendBack.append("\"displayName\":\"").append(search.get().getUser().getUsername()).append("\",");
            sendBack.append("\"displayName\":\"").append(search.get().getUser()).append("\",");
            sendBack.append("\"LikeAmount\":\"").append(search.get().getLikeCount()).append("\",");
            sendBack.append("\"hasVerify\":\"").append(search.get().getIsVerify()).append("\",");
            sendBack.append("\"replyAmount\":\"").append(replyCount).append("\",");
            sendBack.append("\"CreateDate\":\"").append(search.get().getCreateAt()).append("\",");
            sendBack.append("\"detail\":\"").append(search.get().getDetail()).append("\"");
            sendBack.append("}");
        }

        return new ResponseEntity<>(sendBack.toString(), HttpStatus.OK);
    }

    @PostMapping("/comments/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createComment(@RequestBody createComment info){
        Comment newComment = new Comment();
        Long commentId = commentRepository.findMaxId()+1L;
        Post post = postRepository.findById(Long.parseLong(info.getPostID())).get();
        User user = null;
        List<Reply> replyBodies = new LinkedList<>();
        String topic = info.getTopic();
        String detail = info.getDetail();
        Long likeCount = 0L;
        Boolean anonymous = false;
        Boolean isVerify = false;
        PostStatus statusOfComment = null;
        Date createAt = new Date();
        Date updateAt = createAt;
        Set<User> userLikeComment = new HashSet<>();

        newComment.setCommentId(commentId);
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setReplyBodies(replyBodies);
        newComment.setTopic(topic);
        newComment.setDetail(detail);
        newComment.setLikeCount(likeCount);
        newComment.setAnonymous(anonymous);
        newComment.setIsVerify(isVerify);
        newComment.setStatusOfComment(statusOfComment);
        newComment.setCreateAt(createAt);
        newComment.setUpdateAt(updateAt);
        newComment.setUserLikeComment(userLikeComment);

        commentRepository.save(newComment);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
}
