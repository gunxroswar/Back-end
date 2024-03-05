package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.GetBookMarkDto;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class BookmarkController {
    private final JwtService jwtService;
    public UserRepository userRepository;
    public PostRepository postRepository;
    public JdbcTemplate jdbcTemplate;


    public BookmarkController(JwtService jwtService, UserRepository userRepository,
                              PostRepository postRepository,JdbcTemplate jdbcTemplate) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

        @PostMapping("/bookmark")
    public ResponseEntity<String> getBookmark(@RequestBody GetBookMarkDto getBookMarkDto/*,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION*/){

        try {
            //        System.out.println(AUTHORIZATION);
            //        String UID = jwtService.extractUID(AUTHORIZATION);
            //User user = userRepository.findByUid(Long.parseLong(UID));
            User user = userRepository.findByUid(getBookMarkDto.uid);

            Post post = postRepository.findByPostId(getBookMarkDto.postId);
            Set<Post> userBookmarkPosts = user.getBookmarkPosts();
            Set<User> userThatBookmark = post.getUserBookmarks();

            userBookmarkPosts.add(post);
            user.setBookmarkPosts(userBookmarkPosts);
            userRepository.save(user);

            userThatBookmark.add(user);
            post.setUserBookmarks(userThatBookmark);
            postRepository.save(post);

            return ResponseEntity.ok("Bookmark complete");
        } catch (Exception e) {
            // You can handle the exception as per your application's requirement.
            // For example, logging the error or returning an appropriate response to the client.
            e.printStackTrace(); // For logging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to bookmark due to an internal error.");
        }
    }

    @PostMapping("/testbookmark")
    public ResponseEntity<String> testBookmark(@RequestBody @NotNull GetBookMarkDto getBookMarkDto) {
        User user = userRepository.findByUid(getBookMarkDto.uid);
        Post post = postRepository.findByPostId(getBookMarkDto.postId);
        Set<Post> userBookmarkPosts = user.getBookmarkPosts();
        Set<User> userThatBookmark = post.getUserBookmarks();

        userBookmarkPosts.add(post);
        user.setBookmarkPosts(userBookmarkPosts);
        userRepository.save(user);

//        userThatBookmark.add(user);
//        post.setUserBookmarks(userThatBookmark);
//        //postRepository.save(post);

        return ResponseEntity.ok("Add bookmark complete, uid = " + user.getUid() + " postId = " + post.getPostId());
    }


    @PostMapping("/remove-bookmark")
    public ResponseEntity<String> removeBookmarkV2(@RequestBody @NotNull GetBookMarkDto getBookMarkDto){

        String deleteQuery = "DELETE FROM bookmark WHERE user_id = ? AND post_id = ?";
        int deletedRows = jdbcTemplate.update(deleteQuery, getBookMarkDto.uid, getBookMarkDto.postId);

        if (deletedRows > 0) {
            return ResponseEntity.ok("Bookmark removed successfully, uid = " + getBookMarkDto.uid + " postId = " + getBookMarkDto.postId);
        } else {
            return ResponseEntity.ok("Bookmark not found, uid = " + getBookMarkDto.uid + " postId = " + getBookMarkDto.postId);
        }
    }

    @PostMapping("/showbookmark")
    public ResponseEntity<Set> showBookmark(@RequestBody GetBookMarkDto getBookMarkDto){
        User user = userRepository.findById(getBookMarkDto.uid).orElseThrow(
                () -> new UsernameNotFoundException(getBookMarkDto.uid.toString()));
        Set<Post> bookmarkPost = user.getBookmarkPosts();

        return ResponseEntity.ok(bookmarkPost);
    }

}
