package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

//     Comment findByCommentId(Long commentId);
     List<Comment> findByPost(Post post);
     List<Comment> findByUser(User user);

     Long countByPost(Post post);
}
