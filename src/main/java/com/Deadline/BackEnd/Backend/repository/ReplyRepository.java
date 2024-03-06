package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.Reply;
import com.Deadline.BackEnd.Backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {


    List<Reply> findByComment(Comment comment);
    List<Reply> findByUser(User user);
    @Query("SELECT coalesce(max(replyId), 0) FROM Reply")
    Long findMaxId();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM like_reply WHERE reply_id = :reply_id AND user_id = :user_id", nativeQuery = true)
    void deleteLike(@Param("reply_id") Long replyId, @Param("user_id") Long userId);
    Long countByComment(Comment comment);
}
