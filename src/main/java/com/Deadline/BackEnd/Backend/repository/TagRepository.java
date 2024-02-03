package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.ReplyBody;
import com.Deadline.BackEnd.Backend.model.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagName,Long> {
}
