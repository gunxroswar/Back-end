package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

//     Comment findByCommentId(Long commentId);
     List<Comment> findByPost(Post post);
     List<Comment> findByUser(User user);

     @Query("SELECT coalesce(max(commentId), 0) FROM Comment")
     Long findMaxId();
     Long countByPost(Post post);

     @Modifying
     @Transactional
     @Query(value = "DELETE FROM like_comment WHERE comment_id = :comment_id AND user_id = :user_id", nativeQuery = true)
     void deleteLike(@Param("comment_id") Long commentId, @Param("user_id") Long userId);

     Optional<Comment> findById(Long commentId);
}
