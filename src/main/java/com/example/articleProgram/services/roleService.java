package com.example.articleProgram.services;

import com.example.articleProgram.model.Roles;
import com.example.articleProgram.repositories.roleRepository;
import org.springframework.stereotype.Service;

@Service
public class roleService {
    private final roleRepository roleRepository;

    public roleService(com.example.articleProgram.repositories.roleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Roles getRoleByName (String name) {
        for (Roles r : roleRepository.findAll()) {
            if (r.getRole().equals(name)) {
                return r;
            }
        }
        return null;
    }
}
