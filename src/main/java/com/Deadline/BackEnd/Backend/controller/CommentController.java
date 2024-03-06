package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createComment;
import com.Deadline.BackEnd.Backend.Objects.editComment;
import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.ReplyNotFoundException;
import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.ReplyRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;
    public JwtService jwt = new JwtService();

    private User getUserFromAuthHeader(String authorizationHeader){
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            String u = jwt.extractUID(bearerToken);
            return userRepository.findById(Long.parseLong(u)).orElseThrow(() -> new UserNotFoundException(Long.parseLong(u)));
        } catch (Exception e) {
            return null;
        }
    }

    private String commentJSONBuilder(Comment inputComment, User inputUser){
        StringBuilder sendBack = new StringBuilder();
        String ownerName = "";
        if(inputComment.getUser() != null) ownerName = inputComment.getUser().getUsername();
        boolean isLike = false;
        Set<User> userLikeComment = inputComment.getUserLikeComment();
        if(inputUser != null) isLike = userLikeComment.contains(inputUser);
        int replyCount = inputComment.getReplyBodies().size();

        sendBack.append("{");
        sendBack.append("\"id\":\"").append(inputComment.getCommentId()).append("\",");
        sendBack.append("\"profile_name\":\"").append(ownerName).append("\",");
        sendBack.append("\"detail\":\"").append(inputComment.getDetail()).append("\"");
        sendBack.append("\"create_at\":\"").append(inputComment.getCreateAt()).append("\",");
        sendBack.append("\"like_count\":\"").append(inputComment.getLikeCount()).append("\",");
        sendBack.append("\"is_like\":\"").append(isLike).append("\",");
        sendBack.append("\"hasVerify\":\"").append(inputComment.getIsVerify()).append("\",");
        sendBack.append("\"replyCount\":\"").append(replyCount).append("\",");
        sendBack.append("}");

        return sendBack.toString();
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<String> getCommentInPost(@PathVariable(name = "id") Long postId, @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = getUserFromAuthHeader(authorizationHeader);
        Post post = postRepository.findByPostId(postId);
        List<Comment> search = commentRepository.findByPost(post);
        StringBuilder sendBack = new StringBuilder();
        for(int i = 0; i < search.size(); i++) {
            sendBack.append(commentJSONBuilder(search.get(i), user));
            sendBack.append(",");
        }
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
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
    public ResponseEntity<String> getComment(@RequestParam("commentId") Long id, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        Optional<Comment> commentOpt = commentRepository.findById(id);
        StringBuilder sendBack = new StringBuilder();
        if(commentOpt.isEmpty()) sendBack.append("[]");
        else sendBack.append(commentJSONBuilder(commentOpt.get(), user));

        return new ResponseEntity<>(sendBack.toString(), HttpStatus.OK);
    }

    @PostMapping("/comments/create")
    public ResponseEntity<String> createComment(@RequestBody createComment info, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

        Comment newComment = new Comment();
        Long commentId = commentRepository.findMaxId()+1L;
        Post post = postRepository.findById(Long.parseLong(info.getPostID())).get();
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
            String uid_string =jwt.extractUID(bearerToken)  ;
            if(uid_string == null) {return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);}
            Long uid = Long.parseLong(uid_string);
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
            String uid_string =jwt.extractUID(bearerToken)  ;
            if(uid_string == null) {return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);}
            Long uid = Long.parseLong(uid_string);
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
