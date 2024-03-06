package com.Deadline.BackEnd.Backend.controller;

import com.Deadline.BackEnd.Backend.Dto.BookMarkDto;

import com.Deadline.BackEnd.Backend.exception.BookmarkNotFoundException;
import com.Deadline.BackEnd.Backend.exception.PostNotFoundException;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.PostRepository;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import com.Deadline.BackEnd.Backend.service.JwtService;

import com.google.common.net.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.rmi.server.UID;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
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
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/backend_database",
                "root", "Gxz171477940*");
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

    @PostMapping("/addbookmark")
    public ResponseEntity<String> testBookmark(@RequestBody @NotNull BookMarkDto bookMarkDto ,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION) {

        try{
            System.out.println(AUTHORIZATION);
            String UID = jwtService.extractUID(AUTHORIZATION);
            User user = userRepository.findById(Long.parseLong(UID)).orElseThrow(
                    () -> new UsernameNotFoundException(bookMarkDto.uid.toString()));
            Post post = postRepository.findById(bookMarkDto.postId).orElseThrow(
                    () -> new PostNotFoundException(bookMarkDto.postId));
            Set<Post> userBookmarkPosts = user.getBookmarkPosts();
            Set<User> userThatBookmark = post.getUserBookmarks();

            if(userBookmarkPosts.contains(post)){
                return ResponseEntity.badRequest().body("You already bookmarked this post");
            }
            userBookmarkPosts.add(post);
            user.setBookmarkPosts(userBookmarkPosts);
            userRepository.save(user);
            return ResponseEntity.ok("Add bookmark complete, uid = " + user.getUid()
                    + " postId = " + post.getPostId());
        }catch (PostNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/remove-bookmark")
    public ResponseEntity<String> removeBookmarkV2(@RequestBody  BookMarkDto bookMarkDto,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION){

        String deleteQuery = "DELETE FROM bookmark WHERE user_id = ? AND post_id = ?";
        System.out.println(AUTHORIZATION);
        String UID = jwtService.extractUID(AUTHORIZATION);
        Long uid = Long.parseLong(UID);
        int deletedRows = jdbcTemplate.update(deleteQuery, uid, bookMarkDto.postId);

        if (deletedRows > 0) {
            return ResponseEntity.ok("Bookmark removed successfully, uid = " + uid + " postId = " + bookMarkDto.postId);
        } else {
            return ResponseEntity.badRequest().body("Bookmark not found, uid = " + uid + " postId = " + bookMarkDto.postId);
        }
    }

//    @PostMapping("/showbookmark")
//    public ResponseEntity<String> showBookmark(@RequestBody BookMarkDto bookMarkDto){
//
//        try {
//            List<User> user = userRepository.findByUid(bookMarkDto.uid);
//            List<Post> posts = postRepository.findByUser(user.getFirst());
//            User tempUser = user.getFirst();
//
//            Set<Post> bookmarkPost = new HashSet<>();
//            for(Post post: tempUser.getBookmarkPosts()){
//                bookmarkPost.add(post);
//            }
//            return ResponseEntity.ok("user: " + bookmarkPost.size());
//        }catch (BookmarkNotFoundException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: Failed to retrieve bookmarked posts");
//        }
//    }
    @PostMapping("/showbookmarkV2")
    public ResponseEntity<Set>  getBookmarkedPostIds(@RequestBody BookMarkDto bookMarkDto ,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String AUTHORIZATION) {
        Set<Post> bookmarkPost = new HashSet<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT post_id FROM bookmark WHERE user_id = ?")) {

            System.out.println(AUTHORIZATION);
            String UID = jwtService.extractUID(AUTHORIZATION);
            Long newUid = Long.parseLong(UID);
            List<User> user = userRepository.findByUid(newUid);
            statement.setLong(1, newUid);
            ResultSet resultSet = statement.executeQuery();
//            if(!resultSet.next()){
//                return ResponseEntity.badRequest().body(new BookmarkNotFoundException(user.getFirst()).getMessage());
//            }
            while (resultSet.next()) {
                long postId = resultSet.getLong("post_id");
                bookmarkPost.add(postRepository.findByPostId(postId));
            }
            return ResponseEntity.ok(bookmarkPost);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new HashSet<>());
        }
    }

}
