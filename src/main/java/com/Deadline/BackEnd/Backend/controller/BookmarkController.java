package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.GetBookMarkDto;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
public class BookmarkController {
    private final JwtService jwtService;
    private UserRepository userRepository;

    private PostRepository postRepository;

    public BookmarkController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/getbookmark")
    public ResponseEntity<String> getBookmark(@RequestBody GetBookMarkDto getBookMarkDto/*,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION*/){

//        System.out.println(AUTHORIZATION);
//        String UID = jwtService.extractUID(AUTHORIZATION);
        //User user = userRepository.findByUid(Long.parseLong(UID));
        User user = userRepository.findByUid(getBookMarkDto.uid);
        Post post = postRepository.findByPostId(getBookMarkDto.postId);

        Set<Post> userBookmarkPosts = user.getBookmarkPosts();
        userBookmarkPosts.add(post);
        user.setBookmarkPosts(userBookmarkPosts);
        userRepository.save(user);

        Set<User> userThatBookmark = post.getUserBookmarks();
        userThatBookmark.add(user);
        post.setUserBookmarks(userThatBookmark);
        postRepository.save(post);

        return ResponseEntity.ok("Bookmark complete");
    }

    @PostMapping("/removebookmark")
    public ResponseEntity<String> removeBookmark(@RequestBody GetBookMarkDto getBookMarkDto,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION){

        System.out.println(AUTHORIZATION);
        String UID = jwtService.extractUID(AUTHORIZATION);
        User user = userRepository.findByUid(Long.parseLong(UID));
        Post post = postRepository.findByPostId(getBookMarkDto.postId);

        Set<Post> userBookmarkPosts = user.getBookmarkPosts();
        userBookmarkPosts.remove(post);
        user.setBookmarkPosts(userBookmarkPosts);
        userRepository.save(user);

        Set<User> userThatBookmark = post.getUserBookmarks();
        userThatBookmark.remove(user);
        post.setUserBookmarks(userThatBookmark);
        postRepository.save(post);

        return ResponseEntity.ok("Bookmark has been removed");
    }

}
