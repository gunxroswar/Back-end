package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Cookie;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

//   Post findByPostId(Long postId);
    List<Post> findByUser(User user);

    @Query("SELECT coalesce(max(postId), 0) FROM Post")
    Long findMaxId();

    Optional<Post> findByPostId(Long id);

    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "ORDER BY createAt DESC LIMIT 10")
    List<Post> page(Long range);
}
