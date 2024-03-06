package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.Objects.editComment;
import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.ReplyNotFoundException;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.ReplyRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.dialect.SybaseASEDialect;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
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
    public JwtService jwt = new JwtService();

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
    public ResponseEntity<String> createComment(@RequestBody createComment info){
        Comment newComment = new Comment();
        Long commentId = commentRepository.findMaxId()+1L;
        Post post = postRepository.findById(Long.parseLong(info.getPostID())).get();
        User user = null;
        List<Reply> replyBodies = new LinkedList<>();

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

    @PutMapping("/comments")
    public ResponseEntity<String> editComment(@RequestHeader("Authorization") String authorizationHeader,@RequestBody editComment info){
        try
        {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            Comment editComment = commentRepository.findById(Long.getLong(info.getCommentID())).orElseThrow(()->new CommentNotFoundException(Long.getLong(info.getCommentID())));
            if(editComment.getUser().getUid().equals(uid)  ){
                String detail = info.getDetail();

                editComment.setDetail(detail);

                commentRepository.save(editComment);

                return new ResponseEntity<>("edit comment successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("User don't own comment "+ editComment.getCommentId(), HttpStatus.FORBIDDEN);
            }
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/comments")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("commentId") Long commentId){
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            Comment deleteComment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException(commentId));
            if(deleteComment.getUser().getUid().equals(uid)  ) {
                commentRepository.deleteById(commentId);
                return new ResponseEntity<>("delete comment successfully", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<String>("User don't own comment "+ deleteComment.getCommentId(), HttpStatus.FORBIDDEN);
            }
        }catch (CommentNotFoundException e)
        {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }
    }
}
