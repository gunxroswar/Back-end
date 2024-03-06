package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createReply;
import com.Deadline.BackEnd.Backend.Objects.editReply;
import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.ReplyNotFoundException;
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

    @GetMapping("/comments/{id}/replys")
    public ResponseEntity<String> getReplyInComment(@PathVariable(name = "id") Long commentId, @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = getUserFromAuthHeader(authorizationHeader);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) return new ResponseEntity<>("Not found.", HttpStatus.NOT_FOUND);
        List<Reply> search = replyRepository.findByComment(comment.get());
        StringBuilder sendBack = new StringBuilder();
        for(int i = 0; i < search.size(); i++) {
            sendBack.append(replyJSONBuilder(search.get(i), user));
            sendBack.append(",");
        }
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
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
        if(user == null) return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);

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

    @PutMapping("/replys")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> editReply(@RequestBody editReply info, @RequestHeader(value = "Authorization") String authorizationHeader){
        try {
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);
        Long editReplyID = Long.getLong(info.getReplyID());
        Reply editReply = replyRepository.findById(editReplyID).orElseThrow(()->new ReplyNotFoundException(Long.getLong(info.getCommentID())));
        if(editReply.getUser() != user) return new ResponseEntity<String>("User don't own reply "+editReply.getReplyId(), HttpStatus.FORBIDDEN);
        String topic = info.getTopic();
        String detail = info.getDetail();
        Date updateAt = new Date();
        editReply.setTopic(topic);
        editReply.setDetail(detail);
        editReply.setUpdateAt(updateAt);
        replyRepository.save(editReply);
        return new ResponseEntity<>("edit reply successfully", HttpStatus.OK);
        } catch (ReplyNotFoundException e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/replys")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteReply(@RequestParam("replyId") Long replyId, @RequestHeader(value = "Authorization") String authorizationHeader){
        try {
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);
        Reply editReply = replyRepository.findById(replyId).orElseThrow(()->new ReplyNotFoundException(replyId));
        if(editReply.getUser() != user) return new ResponseEntity<String>("User don't own reply "+editReply.getReplyId(), HttpStatus.FORBIDDEN);
            replyRepository.deleteById(replyId);
            return new ResponseEntity<>("delete reply successfully", HttpStatus.OK);
        } catch (ReplyNotFoundException e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }
    }
}
