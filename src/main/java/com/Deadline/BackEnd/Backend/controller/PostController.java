package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Objects.createPost;
import com.Deadline.BackEnd.Backend.Objects.editPost;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.*;
import com.Deadline.BackEnd.Backend.repository.CommentRepository;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.TagRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
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
    @Autowired
    private TagRepository tagRepository;
    private JwtService jwt = new JwtService();

    private Set<TagName> readTag(String inputString){
        List<String> infoTags = new LinkedList<>();
        StringBuilder tagReader = new StringBuilder();
        Character currentChar;
        for(int i = 1; i < inputString.length()-1; i++){
            currentChar = inputString.charAt(i);
            if(currentChar == ','){
                infoTags.add(tagReader.toString());
                tagReader.setLength(0);
                continue;
            }
            tagReader.append(currentChar);
        }
        if(!tagReader.isEmpty()) infoTags.add(tagReader.toString());

        Set<TagName> tagNames = new HashSet<>();
        Optional<TagName> tagname = null;
        for(int i = 0; i < infoTags.size(); i++){
            tagname = tagRepository.findByTagName(infoTags.get(i));
            if(!tagname.isEmpty()) tagNames.add(tagname.get());
        }

        return tagNames;
    }

    private String tagSetToJSONTag(Set<TagName> tagNames){
        if(tagNames.isEmpty()) return "{}";
        StringBuilder JSONTag = new StringBuilder("{");
        for(TagName tag : tagNames){
            JSONTag.append(tag.getTagName() + ",");
        }
        JSONTag.deleteCharAt(JSONTag.length()-1);
        JSONTag.append("}");

        return JSONTag.toString();
    }

    @PostMapping("/posts/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createPost(@RequestBody createPost info){
        Post newPost = new Post();
        Long postId = postRepository.findMaxId()+1L;
        User user = null;
        List<Comment> commentList = new LinkedList<>();
        String topic = info.getTopic();
        Set<TagName> tagNames = readTag(info.getTag());
        String detail = info.getDetail();
        Long likeCount = 0L;
        Boolean anonymous = false;
        Boolean hasVerify = false;
        PostStatus statusOfPost = null;
        Date createAt = new Date();
        Date updateAt = createAt;
        Set<User> userBookmarks = new HashSet<>();
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
        newPost.setUserLikePost(userLikePost);

        postRepository.save(newPost);

        for(TagName tag : tagNames) tag.getPostWithTags().add(newPost);
        tagRepository.saveAll(tagNames);

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
        Set<TagName> tagNames = readTag(info.getTag());
        String detail = info.getDetail();

        editPost.setTopic(topic);
        editPost.setTagNames(tagNames);
        editPost.setDetail(detail);

        postRepository.save(editPost);
        tagRepository.saveAll(tagNames);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getPost(@RequestParam("postId") Long id ,@RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        User user;
        try {
            String u = jwt.extractUID(bearerToken);
            user= userRepository.findById(Long.parseLong(u)).orElseThrow(()-> new UserNotFoundException(Long.parseLong(u)));
        } catch (Exception e){}


        Optional<Post> search = postRepository.findById(id);
        StringBuilder sendBack = new StringBuilder();
        if(search.isEmpty()) sendBack.append("[]");
        //"topic, detail , create_at, like_count, '[]' as taglist"
        else{
            sendBack.append("{");
            Post currentPost = search.get();
            sendBack.append("\"topic\":\"").append(currentPost.getTopic()).append("\",");
            sendBack.append("\"detail\":\"").append(currentPost.getDetail()).append("\",");
            sendBack.append("\"create_at\":\"").append(currentPost.getCreateAt()).append("\",");
            sendBack.append("\"like_count\":\"").append(currentPost.getLikeCount()).append("\",");
            Set<TagName> tagName = tagRepository.findByPostWithTags(currentPost);
            sendBack.append("\"taglist\":\"").append(tagSetToJSONTag(tagName)).append("\"");
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
            Set<TagName> tagName = tagRepository.findByPostWithTags(currentPost);
            sendBack.append("\"taglist\":\"").append(tagSetToJSONTag(tagName)).append("\"");
            subSendBack.append("\"commentCount\":\"").append(commentCount.toString()).append("\"");
            subSendBack.append("},");
            sendBack.append(subSendBack);
            subSendBack.delete(0, subSendBack.length());
        }
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
    }
}
