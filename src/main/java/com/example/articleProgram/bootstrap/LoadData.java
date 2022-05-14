package com.example.articleProgram.bootstrap;

import com.example.articleProgram.model.Article;
import com.example.articleProgram.model.Roles;
import com.example.articleProgram.model.User;
import com.example.articleProgram.repositories.roleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoadData implements CommandLineRunner {

    private final com.example.articleProgram.repositories.userRepository userRepository;
    private final roleRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.example.articleProgram.repositories.articleRepository articleRepository;
    public LoadData(com.example.articleProgram.repositories.userRepository userRepository, roleRepository rolesRepository, PasswordEncoder passwordEncoder, com.example.articleProgram.repositories.articleRepository articleRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Roles r1 = new Roles();
        r1.setRole("USER");
        rolesRepository.save(r1);

        User u1 = new User();
        u1.setUsername("lindi");
        u1.setEmail("lk@gmail.com");
        u1.setPassword(passwordEncoder.encode("1234"));
        u1.getRoles().add(r1);
        userRepository.save(u1);

        User u2 = new User();
        u2.setUsername("lindiTest");
        u2.setEmail("lk@gmail.com");
        u2.setPassword(passwordEncoder.encode("1234"));
        u2.getRoles().add(r1);
        userRepository.save(u2);

        User u3 = new User();
        u3.setUsername("test user");
        u3.setEmail("lk@gmail.com");
        u3.setPassword(passwordEncoder.encode("1234"));
        u3.getRoles().add(r1);
        userRepository.save(u3);

        User u4 = new User();
        u4.setUsername("user4");
        u4.setEmail("lk@gmail.com");
        u4.setPassword(passwordEncoder.encode("1234"));
        u4.getRoles().add(r1);
        userRepository.save(u4);

        u1.getFollowing().add(u2);
        u1.getFollowing().add(u4);
        userRepository.save(u1);

        u2.getFollowers().add(u1);
        u4.getFollowers().add(u1);

        userRepository.save(u2);
        userRepository.save(u4);

        Article a1 = new Article();
        a1.setTitle("Article1");
        a1.setDescription("Desc");
        a1.setAuthor(u2);
        articleRepository.save(a1);

        Article a2 = new Article();
        a2.setTitle("Article2");
        a2.setDescription("Desc");
        a2.setAuthor(u4);
        articleRepository.save(a2);

        Article a3 = new Article();
        a3.setTitle("Article3");
        a3.setDescription("Desc");
        a3.setAuthor(u3);
        articleRepository.save(a3);

        u2.getArticles().add(a1);
        userRepository.save(u2);

        u4.getArticles().add(a2);
        userRepository.save(u4);

        u3.getArticles().add(a3);
        userRepository.save(u3);

        for (User u : userRepository.findAll()) {
            System.out.println(u.getUsername());
            System.out.println(u1.getPassword());
        }
    }
}
