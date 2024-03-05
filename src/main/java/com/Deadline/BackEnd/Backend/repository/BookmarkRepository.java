package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<User, Long> {
}
