package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.ShowPostDto;
import com.Deadline.BackEnd.Backend.Dto.NewPostDto;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundExcetion;
import com.Deadline.BackEnd.Backend.exception.UerNotFoundExcetion;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.PostStatus;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.StatusRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PostTopicController {
    @Autowired
    PostRepository postTopicRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StatusRepository statusRepository;



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
                     .hasVerify(post.getHasVerify())
                     .status("ok")
                     .likeCount(post.getLikeCount())
                     .createAt(post.getCreateAt())
                     .build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(postDto);
    }
//    @PostMapping("/test/posts/create")
//    public ResponseEntity<String> createPost(@RequestBody NewPostDto newPostDto)
//    {
//
//        try {
//            Optional<PostStatus> statusOpt= statusRepository.findById(1L);
//            Optional<User> uerOpt =userRepository.findById(newPostDto.getUID());
//            PostStatus status =statusOpt.orElseThrow(()->new UerNotFoundExcetion(newPostDto.getUID()));
//            User user =uerOpt.orElseThrow(()->new UerNotFoundExcetion(newPostDto.getUID()));
//            Post post = new Post();
//            post.builder()
//                    .postId(1L)
//                    .user(user)
//                    .topic(newPostDto.getTopic())
//                    .detail(newPostDto.getDetail())
//                    .anonymous(newPostDto.getAnonymous())
//                    .hasVerify(false)
//                    .statusOfPost(status)
//                    .build();
//            System.out.println("fkweopfjseorgmoseg");
//            postTopicRepository.save(post);
//
//        } catch (Exception e) {
//            ResponseEntity.badRequest().body(e.toString());
//        }
//    return  new ResponseEntity<String>("success",HttpStatus.CREATED);
//
////        postTopicRepository.save();
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
