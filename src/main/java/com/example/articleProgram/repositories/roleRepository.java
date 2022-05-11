package com.example.articleProgram.repositories;

import com.example.articleProgram.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface roleRepository extends JpaRepository<Roles, Long> {

}
