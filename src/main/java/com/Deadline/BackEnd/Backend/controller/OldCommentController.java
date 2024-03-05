package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OldCommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
//
//    @GetMapping("/posts/{id}/comments")
//    public ResponseEntity<List<Comment>> getCommentInPost(@PathVariable(name = "id") Long postId )
//    {
//        List<Comment> comments;
//        try
//        {
//            Optional<Post> postOpt = postRepository.findById(postId);
//            comments= commentRepository.findByPost(postOpt.orElseThrow());
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(comments);
//    }
//    @PostMapping("/comments")
//    public  ResponseEntity<String> insertComments(@RequestBody @Valid Comment comment)
//    {
//        Optional<Post> postOpt = postRepository.findById(comment.getPost().getPostId());
//        if(postOpt.isEmpty())
//        {
//            return ResponseEntity.badRequest().body("Post is not found");
//        }
//        try {
//            commentRepository.save(comment);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("");
//        }
//        return ResponseEntity.ok().body("ok");
//    }


}
