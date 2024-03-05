package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.editPost;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createPost(@RequestBody createPost info){
        Post newPost = new Post();
        Long postId = postRepository.findMaxId()+1L;
        User user = null;
        List<Comment> commentList = new LinkedList<>();
        String topic = info.getTopic();
        String detail = info.getDetail();
        Long likeCount = 0L;
        Boolean anonymous = false;
        Boolean hasVerify = false;
        PostStatus statusOfPost = null;
        Date createAt = new Date();
        Date updateAt = createAt;
        Set<User> userBookmarks = new HashSet<>();
        Set<TagName> tagNames = new HashSet<>();
        Set<User> userLikePost = new HashSet<>();

        newPost.setPostId(postId);
        newPost.setUser(user);
        newPost.setCommentBodies(commentList);
        newPost.setTopic(topic);
        newPost.setDetail(detail);
        newPost.setLikeCount(likeCount);
        newPost.setAnonymous(anonymous);
        newPost.setHasVerify(hasVerify);
        newPost.setStatusOfPost(statusOfPost);
        newPost.setCreateAt(createAt);
        newPost.setUpdateAt(updateAt);
        newPost.setUserBookmarks(userBookmarks);
        newPost.setTagNames(tagNames);
        newPost.setUserLikePost(userLikePost);

        postRepository.save(newPost);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping("/posts/edit")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> editPost(@RequestBody editPost info){
        Long editpostId= Long.getLong(info.getPostID());
        Optional<Post> postOpt= postRepository.findById(editpostId);
        Post editPost =postOpt.orElseThrow(() -> new PostNotFoundException(editpostId));
//        Post editPost = postRepository.findById(Long.getLong(info.getPostID())).get();
        String topic = info.getTopic();
        String tag = info.getTag();
        String detail = info.getDetail();

        editPost.setTopic(topic);
        editPost.setDetail(detail);

        postRepository.save(editPost);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getPost(@RequestParam("postId") Long id){
        Optional<Post> search = postRepository.findById(id);
        StringBuilder sendBack = new StringBuilder();
        if(search.isEmpty()) sendBack.append("[]");
        //"topic, detail , create_at, like_count, '[]' as taglist"
        else{
            sendBack.append("{");
            sendBack.append("\"topic\":\"").append(search.get().getTopic()).append("\",");
            sendBack.append("\"detail\":\"").append(search.get().getDetail()).append("\",");
            sendBack.append("\"create_at\":\"").append(search.get().getCreateAt()).append("\",");
            sendBack.append("\"like_count\":\"").append(search.get().getLikeCount()).append("\",");
            sendBack.append("\"taglist\":\"").append(search.get().getTagNames()).append("\"");
            sendBack.append("}");
        }

        return new ResponseEntity<>(sendBack.toString(), HttpStatus.OK);
    }

    @GetMapping("/pages")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getPage(@RequestParam("page") int id){
        List<Post> search = postRepository.page(1L);
        StringBuilder sendBack = new StringBuilder();
        StringBuilder subSendBack = new StringBuilder();
        //id, user.profile_name , topic, detail , create_at, like_count, has_verify, '[]' as taglist, comment.commentCount
        for(int i = 0; i < search.size(); i++){
            Post currentPost = search.get(i);
            Long commentCount = commentRepository.countByPost(search.get(i));
            subSendBack.append("{");
            subSendBack.append("\"id\":\"").append(currentPost.getPostId()).append("\",");
            //subSendBack.append("\"profile_name\":\"").append(currentPost.getUser().getUsername()).append("\",");
            subSendBack.append("\"profile_name\":\"").append(currentPost.getUser()).append("\",");
            subSendBack.append("\"topic\":\"").append(currentPost.getTopic()).append("\",");
            subSendBack.append("\"detail\":\"").append(currentPost.getDetail()).append("\",");
            subSendBack.append("\"create_at\":\"").append(currentPost.getCreateAt()).append("\",");
            subSendBack.append("\"like_count\":\"").append(currentPost.getLikeCount()).append("\",");
            subSendBack.append("\"has_verify\":\"").append(currentPost.getHasVerify()).append("\",");
            subSendBack.append("\"taglist\":\"").append(currentPost.getTagNames()).append("\",");
            subSendBack.append("\"commentCount\":\"").append(commentCount.toString()).append("\"");
            subSendBack.append("},");
            sendBack.append(subSendBack);
            subSendBack.delete(0, subSendBack.length());
        }
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
    }
}
