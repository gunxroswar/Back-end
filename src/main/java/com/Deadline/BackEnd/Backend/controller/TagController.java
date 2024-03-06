package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.exception.UserNotFoundException;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.TagName;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.TagRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class TagController {
    @Autowired
    private PostRepository postRepository;
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
        sendBack.append("}");

        return sendBack.toString();
    }
    @GetMapping("/posts/filterTag")
    public ResponseEntity<String> filterTag(@RequestParam("tagName") String tagName,
                                            @RequestParam("timeStamp") String timeStamp,
                                            @RequestHeader(value = "Authorization") String authorizationHeader){
        User user = getUserFromAuthHeader(authorizationHeader);
        if(Objects.equals(timeStamp, "0")) timeStamp = "6942-01-01 12:12:12.420";
        Optional<TagName> tagNameOpt = tagRepository.findByTagName(tagName);
        if(tagNameOpt.isEmpty()) return new ResponseEntity<>("[]", HttpStatus.NOT_FOUND);

        List<Post> search = postRepository.pageWithTag(tagNameOpt.get(), Timestamp.valueOf(timeStamp));
        StringBuilder sendBack = new StringBuilder();
        for(int i = 0; i < search.size(); i++) {
            sendBack.append(postJSONBuilder(search.get(i), user));
            sendBack.append(",");
        }
        if(!sendBack.isEmpty()) sendBack.deleteCharAt(sendBack.length()-1);

        return new ResponseEntity<>("[" + sendBack + "]", HttpStatus.OK);
    }
}
