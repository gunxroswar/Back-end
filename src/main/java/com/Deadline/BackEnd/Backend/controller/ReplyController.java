package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createReply;
import com.Deadline.BackEnd.Backend.Objects.editPost;
import com.Deadline.BackEnd.Backend.Objects.editReply;
import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.ReplyRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ReplyController {
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    private JwtService jwt = new JwtService();

    private User getUserFromAuthHeader(String authorizationHeader){
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            String u = jwt.extractUID(bearerToken);
            return userRepository.findById(Long.parseLong(u)).orElseThrow(() -> new UserNotFoundException(Long.parseLong(u)));
        } catch (Exception e) {
            return null;
        }
    }

    private String replyJSONBuilder(Reply inputReply, User inputUser){
        StringBuilder sendBack = new StringBuilder();
        String ownerName = "";
        if(inputReply.getUser() != null) ownerName = inputReply.getUser().getUsername();
        boolean isLike = false;
        Set<User> userLikeReply = inputReply.getUserLikeReply();
        if(inputUser != null) isLike = userLikeReply.contains(inputUser);

        sendBack.append("{");
        sendBack.append("\"ReplyID\":\"").append(inputReply.getReplyId()).append("\",");
        sendBack.append("\"displayName\":\"").append(ownerName).append("\",");
        sendBack.append("\"LikeAmount\":\"").append(inputReply.getLikeCount()).append("\",");
        sendBack.append("\"isLike\":\"").append(isLike).append("\",");
        sendBack.append("\"hasVerify\":\"").append(inputReply.getIsVerify()).append("\",");
        sendBack.append("\"CreateDate\":\"").append(inputReply.getCreateAt()).append("\",");
        sendBack.append("\"detail\":\"").append(inputReply.getDetail()).append("\"");
        sendBack.append("}");

        return sendBack.toString();
    }

    @GetMapping("/replys")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getReply(@RequestParam("replyId") Long id, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        Optional<Reply> replyOpt = replyRepository.findById(id);
        if(replyOpt.isEmpty()) return new ResponseEntity<>("[]", HttpStatus.NOT_FOUND);
        String reply = "[" + replyJSONBuilder(replyOpt.get(), user) + "]";

        return new ResponseEntity<>(reply, HttpStatus.OK);
    }

    @PostMapping("/replys/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createReply(@RequestBody createReply info, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

        Reply newReply = new Reply();
        Long replyId = replyRepository.findMaxId()+1L;
        Optional<Comment> comment = commentRepository.findById(Long.parseLong(info.getCommentID()));
        if(comment.isEmpty()) return new ResponseEntity<>("???", HttpStatus.BAD_REQUEST);
        String topic = info.getTopic();
        String detail = info.getDetail();
        Long likeCount = 0L;
        Boolean anonymous = false;
        Boolean isVerify = false;
        PostStatus statusOfReply = null;
        Date createAt = new Date();
        Date updateAt = createAt;
        Set<User> userLikeReply = new HashSet<>();

        newReply.setReplyId(replyId);
        newReply.setComment(comment.get());
        newReply.setUser(user);
        newReply.setTopic(topic);
        newReply.setDetail(detail);
        newReply.setLikeCount(likeCount);
        newReply.setAnonymous(anonymous);
        newReply.setIsVerify(isVerify);
        newReply.setStatusOfReply(statusOfReply);
        newReply.setCreateAt(createAt);
        newReply.setUpdateAt(updateAt);
        newReply.setUserLikeReply(userLikeReply);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping("/replys/edit")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> editReply(@RequestBody editReply info, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        Long editReplyID = Long.getLong(info.getReplyID());
        Optional<Reply> replyOpt = replyRepository.findById(editReplyID);
        if(replyOpt.isEmpty()) return new ResponseEntity<>("???", HttpStatus.BAD_REQUEST);
        if(replyOpt.get().getUser() != user) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

        String topic = info.getTopic();
        String detail = info.getDetail();
        Date updateAt = new Date();

        Reply reply = replyOpt.get();
        reply.setTopic(topic);
        reply.setDetail(detail);
        reply.setUpdateAt(updateAt);

        replyRepository.save(reply);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @GetMapping("/replys/delete")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteReply(@RequestParam("replyId") Long id, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        Optional<Reply> replyOpt = replyRepository.findById(id);
        if(replyOpt.isEmpty()) return new ResponseEntity<>("Where is this post?", HttpStatus.NOT_FOUND);
        if(replyOpt.get().getUser() != user) return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

        replyRepository.deleteById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
