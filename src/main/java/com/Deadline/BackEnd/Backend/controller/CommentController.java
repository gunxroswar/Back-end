package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getCommentInPost(@RequestParam(name = "postId") Long postId )
    {
        List<Comment> comments;
        Post post = postRepository.getById(postId);
        comments= commentRepository.findByPost(post);

        return ResponseEntity.ok().body(comments);
    }

}
