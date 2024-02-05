package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.ShowPostDto;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundExcetion;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PostTopicController {
    @Autowired
    PostRepository postTopicRepository;



    //    @GetMapping("/test/posts")
//    public List<Post> getPage(@RequestParam(name="page") Long page)
//    {
//        return  null;
//    }
    @GetMapping("/test/posts")
    public ResponseEntity<ShowPostDto> getPost(@RequestParam("postId") Long id)
    {   ShowPostDto postDto =new ShowPostDto();
        try
        {
            Optional<Post> postOpt= postTopicRepository.findById(id);
            Post post =postOpt.orElseThrow(() -> new PostNotFoundExcetion(id));
            String showName;
            if (post.getAnonymous()){
                showName ="Anonymous";
            }
            else
            {
                showName = post.getUser().getProfileName();
            }
             postDto.builder()
                     .postId(post.getPostId())
                     .UID(post.getUser().getUid())
                     .name(showName)
                     .topic(post.getTopic())
                     .detail(post.getDetail())
                     .anonymous(post.getAnonymous())
                     .taglist(new ArrayList<>())
                     .status("ok")
                     .likeCount(post.getLikeCount())
                     .createAt(post.getCreateAt());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(postDto);
    }
//    @PostMapping("/posts/create")
//    public void createPost(@RequestBody PostBody postBody)
//    {
//        postTopicRepository.save(postBody);
//    }
//    @PutMapping("post/{id}")
//    public  void editPost(@RequestBody PostBody postBody,@PathVariable(name = "id") Long id)
//    {
//
//    }
//
//    @DeleteMapping("post/{id}")
//    public void  deletePost(@PathVariable(name = "id") Long uid)
//    {
//
//    }
//
//
//

}
