package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.BookMarkDto;

import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
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

//        @PostMapping("/bookmark")
//    public ResponseEntity<String> getBookmark(@RequestBody bookMarkDto bookMarkDto/*,
//                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION*/){
//
//        try {
//            //        System.out.println(AUTHORIZATION);
//            //        String UID = jwtService.extractUID(AUTHORIZATION);
//            //User user = userRepository.findByUid(Long.parseLong(UID));
//            User user = userRepository.findByUid(bookMarkDto.uid);
//
//            Post post = postRepository.findByPostId(bookMarkDto.postId);
//            Set<Post> userBookmarkPosts = user.getBookmarkPosts();
//            Set<User> userThatBookmark = post.getUserBookmarks();
//
//            userBookmarkPosts.add(post);
//            user.setBookmarkPosts(userBookmarkPosts);
//            userRepository.save(user);
//
//            userThatBookmark.add(user);
//            post.setUserBookmarks(userThatBookmark);
//            postRepository.save(post);
//
//            return ResponseEntity.ok("Bookmark complete");
//        } catch (Exception e) {
//            // You can handle the exception as per your application's requirement.
//            // For example, logging the error or returning an appropriate response to the client.
//            e.printStackTrace(); // For logging purposes
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to bookmark due to an internal error.");
//        }
//    }

    @PostMapping("/testbookmark")
    public ResponseEntity<String> testBookmark(@RequestBody @NotNull BookMarkDto bookMarkDto) {
        User user = userRepository.findById(bookMarkDto.uid).orElseThrow(
                () -> new UsernameNotFoundException(bookMarkDto.uid.toString()));
        Post post = postRepository.findById(bookMarkDto.postId).orElseThrow(
                () -> new PostNotFoundException(bookMarkDto.postId));
        Set<Post> userBookmarkPosts = user.getBookmarkPosts();
        Set<User> userThatBookmark = post.getUserBookmarks();

        userBookmarkPosts.add(post);
        user.setBookmarkPosts(userBookmarkPosts);
        userRepository.save(user);

//        userThatBookmark.add(user);
//        post.setUserBookmarks(userThatBookmark);
//        //postRepository.save(post);

        return ResponseEntity.ok("Add bookmark complete, uid = " + user.getUid() + " postId = " + post.getPostId() + " IsBookmarkEmpty: " + userBookmarkPosts.isEmpty());
    }


    @PostMapping("/remove-bookmark")
    public ResponseEntity<String> removeBookmarkV2(@RequestBody  BookMarkDto bookMarkDto){

        String deleteQuery = "DELETE FROM bookmark WHERE user_id = ? AND post_id = ?";
        int deletedRows = jdbcTemplate.update(deleteQuery, bookMarkDto.uid, bookMarkDto.postId);

        if (deletedRows > 0) {
            return ResponseEntity.ok("Bookmark removed successfully, uid = " + bookMarkDto.uid + " postId = " + bookMarkDto.postId);
        } else {
            return ResponseEntity.ok("Bookmark not found, uid = " + bookMarkDto.uid + " postId = " + bookMarkDto.postId);
        }
    }

    @PostMapping("/showbookmark")
    public ResponseEntity<Boolean> showBookmark(@RequestBody BookMarkDto bookMarkDto){
        User user = userRepository.findById(bookMarkDto.uid).orElseThrow(
                () -> new UsernameNotFoundException(bookMarkDto.uid.toString()));
        Set<Post> bookmarkPost = user.getBookmarkPosts();
        Boolean isEmpty = bookmarkPost.isEmpty();

        return ResponseEntity.ok(isEmpty);
    }

}
