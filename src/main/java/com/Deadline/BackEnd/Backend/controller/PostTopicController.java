package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.model.PostBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostTopicController {
    @GetMapping("/posts")
    public List<PostBody> getPage(@RequestParam(name="page") Long page)
    {
        return  null;
    }
    @GetMapping("post/{id}")
    public  PostBody getPost(@PathVariable(name = "id") Long id)
    {
        return null;
    }
    @PostMapping("/posts/create")
    public void createPost(@RequestBody PostBody postBody)
    {

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
