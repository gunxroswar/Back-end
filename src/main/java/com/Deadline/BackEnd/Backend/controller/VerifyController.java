package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.exception.CommentNotFoundExcetion;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundExcetion;
import com.Deadline.BackEnd.Backend.exception.UerNotFoundExcetion;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> verifyComment(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("comment_id") long commentId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        String uid_string=jwt.extractUID(bearerToken);
        try{
            Optional<Comment> commentOpt= commentRepository.findById(commentId);
            Comment comment =commentOpt.orElseThrow(() -> new CommentNotFoundExcetion(commentId));
            Post post= comment.getPost();
            if(post.getUser().getUid().equals(Long.parseLong(uid_string))   ){
                comment.setIsVerify(true);
                post.setHasVerify(true);
                postRepository.save(post);
                commentRepository.save(comment);

                return new ResponseEntity<String>("Comment with ID " + commentId + " is verified", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("User don't own post "+ post.getPostId(), HttpStatus.FORBIDDEN);
            }

        }catch (CommentNotFoundExcetion e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }


    }

    @PutMapping ("/comments/unverify")
    public ResponseEntity<String> unverifyComment(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("comment_id") long commentId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        String uid_string=jwt.extractUID(bearerToken);
        try{
            Optional<Comment> commentOpt= commentRepository.findById(commentId);
            Comment comment =commentOpt.orElseThrow(() -> new CommentNotFoundExcetion(commentId));
            Post post= comment.getPost();
            if(post.getUser().getUid().equals(Long.parseLong(uid_string))  ){
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
        }catch (CommentNotFoundExcetion e) {
            return new ResponseEntity<String>(e.toString(),HttpStatus.NOT_FOUND);
        }


    }

}
