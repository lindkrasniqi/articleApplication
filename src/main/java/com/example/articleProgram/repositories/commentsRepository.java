package com.example.articleProgram.repositories;

import com.example.articleProgram.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface commentsRepository extends JpaRepository<Comments, Long> {
}
