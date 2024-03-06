package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

//   Post findByPostId(Long postId);
    List<Post> findByUser(User user);
    Post findByPostId(Long postId);

    void deleteByPostId(Long postId);

    @Query("SELECT coalesce(max(postId), 0) FROM Post")
    Long findMaxId();


    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "WHERE p.createAt < :timeStamp AND :tagName MEMBER OF p.tagNames\n" +
            "ORDER BY createAt DESC LIMIT 10")
    List<Post> pageWithTag(@Param("tagName") TagName tagName, @Param("timeStamp") Timestamp timeStamp);

    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "WHERE p.createAt < :timeStamp \n" +
            "ORDER BY createAt DESC LIMIT 10")
    List<Post> page(@Param("timeStamp") Timestamp timeStamp);

    Page<Post> findByUser(User user, PageRequest pageRequest);

    Long countByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM like_post WHERE post_id = :post_id AND user_id = :user_id", nativeQuery = true)
    void deleteLike(@Param("post_id") Long postId, @Param("user_id") Long userId);


//    @Query("SELECT p \n" +
//            "FROM Post p \n" +
//            "Where p.ower_id = :UID \n" +
//            "ORDER BY createAt DESC LIMIT 10")
//    List<Post> myPage(@Param("UID")Long UID);
}
