package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Comment;
import com.Deadline.BackEnd.Backend.model.Reply;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {


    List<Reply> findByComment(Comment comment);
    List<Reply> findByUser(User user);
}
