package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

//   Post findByPostId(Long postId);
    List<Post> findByUser(User user);



}
