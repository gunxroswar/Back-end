package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createReply;
import com.Deadline.BackEnd.Backend.Objects.editReply;
import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.ReplyNotFoundException;
import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.PostStatus;
import com.Deadline.BackEnd.Backend.model.Reply;
import com.Deadline.BackEnd.Backend.model.User;
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

    public JwtService jwt = new JwtService();

    @GetMapping("/replys")
    public ResponseEntity<String> getReply(@RequestParam("replyId") Long id){
        Optional<Reply> replyOpt = replyRepository.findById(id);
        if(replyOpt.isEmpty()) return new ResponseEntity<>("[]", HttpStatus.NOT_FOUND);
        Reply reply = replyOpt.get();
        String displayName = userRepository.findByUid(reply.getReplyId()).getFirst().getUsername();
        StringBuilder sendBack = new StringBuilder("[{");
        sendBack.append("\"ReplyID\":\"").append(reply.getReplyId()).append("\",");
        //sendBack.append("\"displayName\":\"").append(reply).append("\",");
        sendBack.append("\"displayName\":\"").append(displayName).append("\",");
        sendBack.append("\"LikeAmount\":\"").append(reply.getLikeCount()).append("\",");
        sendBack.append("\"hasVerify\":\"").append(reply.getIsVerify()).append("\",");
        sendBack.append("\"CreateDate\":\"").append(reply.getCreateAt()).append("\",");
        sendBack.append("\"detail\":\"").append(reply.getDetail()).append("\"");
        sendBack.append("}]");

        return new ResponseEntity<>(sendBack.toString(), HttpStatus.OK);
    }

    @PostMapping("/replys/create")
    public ResponseEntity<String> createReply(@RequestBody createReply info){
        Reply newReply = new Reply();
        Long replyId = replyRepository.findMaxId()+1L;
        Comment comment = commentRepository.findById(Long.parseLong(info.getCommentID())).get();
        User user = null;
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
        newReply.setComment(comment);
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
    public ResponseEntity<String> editReply(@RequestHeader("Authorization") String authorizationHeader,@RequestBody editReply info){
        try{
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            Reply editReply = replyRepository.findById(Long.getLong(info.getReplyID())).orElseThrow(()->new ReplyNotFoundException(Long.getLong(info.getReplyID())));
            if(editReply.getUser().getUid().equals(uid)  ){
                String topic = info.getTopic();
                String detail = info.getDetail();

                editReply.setTopic(topic);
                editReply.setDetail(detail);

                replyRepository.save(editReply);

                return new ResponseEntity<String>("edit reply successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("User don't own reply "+ editReply.getReplyId(), HttpStatus.FORBIDDEN);
            }
        }catch (ReplyNotFoundException e)
        {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/replys")
    public ResponseEntity<String> deleteReply(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("replyId") Long replyId){
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            Reply deleteReply = replyRepository.findById(replyId).orElseThrow(()->new ReplyNotFoundException(replyId));
            if(deleteReply.getUser().getUid().equals(uid)  ) {
                replyRepository.deleteById(replyId);
                return new ResponseEntity<>("delete reply successfully", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<String>("User don't own reply "+ deleteReply.getReplyId(), HttpStatus.FORBIDDEN);
            }
        }catch (ReplyNotFoundException e)
        {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }
    }
}
