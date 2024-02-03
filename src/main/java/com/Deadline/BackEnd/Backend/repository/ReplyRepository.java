package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.ReplyBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyBody,Long> {
}
