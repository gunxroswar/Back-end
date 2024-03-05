package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsername(String username);

    List<User> findByProfileName(String name);

    User findByUid(Long uid);

    Boolean existsByUsername(String username);

    Boolean existsByProfileName(String name);
}
