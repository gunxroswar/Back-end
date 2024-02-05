package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

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


}
