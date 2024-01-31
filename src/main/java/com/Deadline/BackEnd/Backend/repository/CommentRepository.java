package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.CommentBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentBody,Long> {
}
