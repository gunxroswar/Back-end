package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Post;
import com.Deadline.BackEnd.Backend.model.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<TagName,Long> {
    public TagName findByTagName(TagName tagName);

    public Set<TagName> findByPostWithTags(Post post);
    public Optional<TagName> findByTagName(String tagName);
}
