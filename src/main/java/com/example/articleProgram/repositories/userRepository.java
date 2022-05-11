package com.example.articleProgram.repositories;

import com.example.articleProgram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<User, Long> {
}
