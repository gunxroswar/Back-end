package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public List<User> findByUsername(String username);

    public List<User> findByProfileName(String username);

    public List<User> findByUid(Long uid);
}
