package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.StatusRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import com.fasterxml.jackson.databind.node.POJONode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class VerifyController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    public JwtService jwt = new JwtService();


    @PutMapping ("/comments/verify")
    public ResponseEntity<String> verifyComment(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("comment_id") long commentId) {
        try{
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            String uid_string =jwt.extractUID(bearerToken)  ;
            if(uid_string == null) {return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);}
            Long uid = Long.parseLong(uid_string);
            Optional<Comment> commentOpt= commentRepository.findById(commentId);
            Comment comment =commentOpt.orElseThrow(() -> new CommentNotFoundException(commentId));
            Post post= comment.getPost();
            if(post.getUser().getUid().equals(uid)   ){
                comment.setIsVerify(true);
                post.setHasVerify(true);
                postRepository.save(post);
                commentRepository.save(comment);

                return new ResponseEntity<String>("Comment with ID " + commentId + " is verified", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("User don't own post "+ post.getPostId(), HttpStatus.FORBIDDEN);
            }

        }catch (CommentNotFoundException e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }


    }

    @PutMapping ("/comments/unverify")
    public ResponseEntity<String> unverifyComment(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("comment_id") long commentId) {
        try{
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            String uid_string =jwt.extractUID(bearerToken)  ;
            if(uid_string == null) {return new ResponseEntity<>("Authorization is NULL", HttpStatus.UNAUTHORIZED);}
            Long uid = Long.parseLong(uid_string);
            Optional<Comment> commentOpt= commentRepository.findById(commentId);
            Comment comment =commentOpt.orElseThrow(() -> new CommentNotFoundException(commentId));
            Post post= comment.getPost();
            if(post.getUser().getUid().equals(uid)  ){
                comment.setIsVerify(false);
                boolean hasVerify = false;
                for (Comment commentInPost : commentRepository.findByPost(post)) {
                        if(commentInPost.getIsVerify())
                        {
                            hasVerify = true;
                            break;
                        }
                }
                post.setHasVerify(hasVerify);
                postRepository.save(post);
                commentRepository.save(comment);
                return new ResponseEntity<String>("Comment with ID " + commentId + " is unverified", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("User don't own post "+ post.getPostId(), HttpStatus.FORBIDDEN);
            }
        }catch (CommentNotFoundException e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }


    }

}
