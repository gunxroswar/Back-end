package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.exception.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findById(String UID);
}
