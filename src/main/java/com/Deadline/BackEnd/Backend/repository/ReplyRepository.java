package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.Reply;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {


    List<Reply> findByComment(Comment comment);
    List<Reply> findByUser(User user);
    @Query("SELECT coalesce(max(replyId), 0) FROM Reply")
    Long findMaxId();
    Long countByComment(Comment comment);
}
