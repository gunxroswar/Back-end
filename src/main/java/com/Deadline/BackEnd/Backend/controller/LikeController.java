package com.Deadline.BackEnd.Backend.controller;
import com.Deadline.BackEnd.Backend.exception.CommentNotFoundException;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.exception.ReplyNotFoundException;
import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.Reply;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.ReplyRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class LikeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;
    public JwtService jwt = new JwtService();

    @PutMapping("/post/like")
    public ResponseEntity<String> likePost(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("postId") Long postId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            System.out.println("postID = "+(postId));
            Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= post.getUserLikePost();
            likeSet.add(user);
            post.setUserLikePost(likeSet);
            post.setLikeCount((long) likeSet.size());
            postRepository.save(post);
            return new ResponseEntity<>("Post with ID " + postId + " is liked", HttpStatus.OK);

        } catch (PostNotFoundException|UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/post/unlike")
    public ResponseEntity<String> unlikePost(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("postId") Long postId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= post.getUserLikePost();
            likeSet.remove(user);
            post.setUserLikePost(likeSet);
            post.setLikeCount((long) likeSet.size());
            postRepository.save(post);
            return new ResponseEntity<>("Post with ID " + postId + " is unliked", HttpStatus.OK);

        } catch (PostNotFoundException|UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/like")
    public ResponseEntity<String> likeComment(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("commentId") Long commentId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= comment.getUserLikeComment();
            likeSet.add(user);
            comment.setUserLikeComment(likeSet);
            comment.setLikeCount((long) likeSet.size());
            commentRepository.save(comment);
            return new ResponseEntity<>("Comment with ID " + commentId + " is liked", HttpStatus.OK);

        } catch (CommentNotFoundException | UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/unlike")
    public ResponseEntity<String> unlikeComment(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("commentId") Long commentId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= comment.getUserLikeComment();
            likeSet.remove(user);
            comment.setUserLikeComment(likeSet);
            comment.setLikeCount((long) likeSet.size());
            commentRepository.save(comment);
            return new ResponseEntity<>("Comment with ID " + commentId + " is liked", HttpStatus.OK);

        } catch (CommentNotFoundException | UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reply/like")
    public ResponseEntity<String> likeReply(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("replyId") Long replyId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= reply.getUserLikeReply();
            likeSet.add(user);
            reply.setUserLikeReply(likeSet);
            reply.setLikeCount((long) likeSet.size());
            replyRepository.save(reply);
            return new ResponseEntity<>("Reply with ID " + replyId + " is liked", HttpStatus.OK);

        } catch (ReplyNotFoundException | UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reply/unlike")
    public ResponseEntity<String> unlikeReply(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("replyId") Long replyId) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        Long uid = Long.parseLong(jwt.extractUID(bearerToken));
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));
            User user = userRepository.findById(uid).orElseThrow(()->new UserNotFoundException(uid));
            Set<User> likeSet= reply.getUserLikeReply();
            likeSet.remove(user);
            reply.setUserLikeReply(likeSet);
            reply.setLikeCount((long) likeSet.size());
            replyRepository.save(reply);
            return new ResponseEntity<>("Reply with ID " + replyId + " is unliked", HttpStatus.OK);

        } catch (ReplyNotFoundException | UserNotFoundException e  ) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

}
