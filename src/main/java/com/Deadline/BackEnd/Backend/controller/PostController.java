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
import org.hibernate.type.descriptor.java.JdbcTimestampJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    private User getUserFromAuthHeader(String authorizationHeader){
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            String u = jwt.extractUID(bearerToken);
            return userRepository.findById(Long.parseLong(u)).orElseThrow(() -> new UserNotFoundException(Long.parseLong(u)));
        } catch (Exception e) {
            return null;
        }
    }

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

    private String postJSONBuilder(Post inputPost, User inputUser){
        // New return JSON
        StringBuilder sendBack = new StringBuilder();
        // Get owner name of inputPost
        String ownerName = "";
        if(inputPost.getUser() != null) ownerName = inputPost.getUser().getUsername();
        // Is inputUser like inputPost
        boolean isLike = false;
        Set<User> userLikePost = inputPost.getUserLikePost();
        if(inputUser != null) isLike = userLikePost.contains(inputUser);
        // Get all tagName of inputPost
        Set<TagName> tagName = tagRepository.findByPostWithTags(inputPost);
        // Count comment of inputPost
        int commentCount = inputPost.getCommentBodies().size();

        sendBack.append("{");
        sendBack.append("\"id\":\"").append(inputPost.getPostId()).append("\",");
        sendBack.append("\"profile_name\":\"").append(ownerName).append("\",");
        sendBack.append("\"topic\":\"").append(inputPost.getTopic()).append("\",");
        sendBack.append("\"detail\":\"").append(inputPost.getDetail()).append("\",");
        sendBack.append("\"create_at\":\"").append(inputPost.getCreateAt()).append("\",");
        sendBack.append("\"like_count\":\"").append(inputPost.getLikeCount()).append("\",");
        sendBack.append("\"is_like\":\"").append(isLike).append("\",");
        sendBack.append("\"has_verify\":\"").append(inputPost.getHasVerify()).append("\",");
        sendBack.append("\"taglist\":\"").append(tagSetToJSONTag(tagName)).append("\",");
        sendBack.append("\"commentCount\":\"").append(commentCount).append("\"");
        sendBack.append("},");

        return sendBack.toString();
    }

    @PostMapping("/posts/create")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> createPost(@RequestBody createPost info, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("Fuck you", HttpStatus.FORBIDDEN);

        Post newPost = new Post();
        Long postId = postRepository.findMaxId()+1L;
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
    public ResponseEntity<String> editPost(@RequestBody editPost info, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("Fuck you", HttpStatus.FORBIDDEN);
        Long editpostId= Long.getLong(info.getPostID());
        Optional<Post> postOpt = postRepository.findById(editpostId);
        Post editPost = postOpt.orElseThrow(() -> new PostNotFoundException(editpostId));
        if(editPost.getUser() != user) return new ResponseEntity<>("Fuck you", HttpStatus.FORBIDDEN);
        String topic = info.getTopic();
        Set<TagName> tagNames = readTag(info.getTag());
        String detail = info.getDetail();
        Date updateAt = new Date();

        editPost.setTopic(topic);
        editPost.setTagNames(tagNames);
        editPost.setDetail(detail);
        editPost.setUpdateAt(updateAt);

        postRepository.save(editPost);
        tagRepository.saveAll(tagNames);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @GetMapping("/posts/delete")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deletePost(@RequestParam("postId") Long id, @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(user == null) return new ResponseEntity<>("Fuck you", HttpStatus.FORBIDDEN);
        Optional<Post> post = postRepository.findById(id);
        if(post.isEmpty()) return new ResponseEntity<>("Where is this post?", HttpStatus.NOT_FOUND);
        if(post.get().getUser() != user) return new ResponseEntity<>("Fuck you", HttpStatus.FORBIDDEN);

        Set<TagName> tagNames = tagRepository.findByPostWithTags(post.get());
        for(TagName tagName : tagNames) tagNames.remove(post.get());
        tagRepository.saveAll(tagNames);
        postRepository.deleteByPostId(post.get().getPostId());

        return new ResponseEntity<>("OK.", HttpStatus.OK);
    }

    @GetMapping("/posts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getPost(@RequestParam("postId") Long id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        Optional<Post> post = postRepository.findById(id);
        StringBuilder sendBack = new StringBuilder();
        if(post.isEmpty()) sendBack.append("[]");
        else sendBack.append(postJSONBuilder(post.get(), user));

        return new ResponseEntity<>(sendBack.toString(), HttpStatus.OK);
    }

    @GetMapping("/pages")
    public ResponseEntity<String> getPage(@RequestParam("timeStamp") String timeStamp, @RequestHeader(value = "Authorization", required = false) java.lang.String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(Objects.equals(timeStamp, "0")) timeStamp = "6942-01-01 12:12:12.420";

        List<Post> search = postRepository.page(Timestamp.valueOf(timeStamp));
        StringBuilder sendBack = new StringBuilder();
        for(int i = 0; i < search.size(); i++) sendBack.append(postJSONBuilder(search.get(i), user));
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
    }

    @GetMapping("/mypages/amount")
    public  ResponseEntity<String>  getNumMyPage(@RequestHeader("Authorization") java.lang.String authorizationHeader)
    {
         int pageSize = 10;
        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
            Long numAll = postRepository.countByUser(user);
            long numPage = Math.ceilDiv(numAll,pageSize);
            return new ResponseEntity<>(Long.toString(numPage), HttpStatus.OK);
        } catch (NumberFormatException | UserNotFoundException e) {
            return  new ResponseEntity<>(e.toString(),HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/mypages")
    public ResponseEntity<String> getMyPage(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("page") int pageId)
    {

        try {
            String bearerToken = authorizationHeader.replace("Bearer ", "");
            Long uid = Long.parseLong(jwt.extractUID(bearerToken));
            User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
            PageRequest pageSize10AndSortByCreateAt=PageRequest.of(pageId, 10, Sort.by("createAt"));
            List<Post> search = postRepository.findByUser(user, pageSize10AndSortByCreateAt).getContent();
            StringBuilder sendBack = new StringBuilder();
            StringBuilder subSendBack = new StringBuilder();
            //id, user.profile_name , topic, detail , create_at, like_count, has_verify, '[]' as taglist, comment.commentCount
            for (int i = 0; i < search.size(); i++) {
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
                subSendBack.append("\"taglist\":\"").append(tagSetToJSONTag(tagName)).append("\",");
                subSendBack.append("\"commentCount\":\"").append(commentCount.toString()).append("\"");
                subSendBack.append("},");
                sendBack.append(subSendBack);
                subSendBack.delete(0, subSendBack.length());
            }
            if (!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length() - 1);

            return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.toString(),HttpStatus.NOT_FOUND);
        }

    }




}
