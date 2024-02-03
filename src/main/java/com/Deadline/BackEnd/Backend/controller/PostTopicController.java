package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.PostBody;
import com.Deadline.BackEnd.Backend.repository.PostTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PostTopicController {
    @Autowired
    PostTopicRepository postTopicRepository;
    @GetMapping("/posts")
    public List<PostBody> getPage(@RequestParam(name="page") Long page)
    {
        return  postTopicRepository.findAll();
    }
    @GetMapping("post/{id}")
    public Optional<PostBody> getPost(@PathVariable(name = "id") Long id)
    {

        return postTopicRepository.findById(id);
    }
    @PostMapping("/posts/create")
    public void createPost(@RequestBody PostBody postBody)
    {
        postTopicRepository.save(postBody);
    }
    @PutMapping("post/{id}")
    public  void editPost(@RequestBody PostBody postBody,@PathVariable(name = "id") Long id)
    {

    }

    @DeleteMapping("post/{id}")
    public void  deletePost(@PathVariable(name = "id") Long id)
    {
        
    }




}
