package com.blog.repositories;

import com.blog.models.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository< Tags, String> {

    Optional<Tags> findByName(String name);
}
