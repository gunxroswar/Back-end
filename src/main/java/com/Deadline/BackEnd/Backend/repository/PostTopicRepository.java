package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.PostBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTopicRepository extends JpaRepository<PostBody,Long> {

}
