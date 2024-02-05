package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<PostStatus,Long> {
}
