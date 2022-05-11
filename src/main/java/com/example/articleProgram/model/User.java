package com.example.articleProgram.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    private String jobDescription;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<Article> articles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles = new ArrayList<>();

    @ElementCollection
    private List<Long> followersId = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonBackReference
    private List<User> following = new ArrayList<>();

    @ManyToMany(mappedBy = "following")
    @JsonBackReference
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "article_id"))
    private List<Article> favorites = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private List<Article> upVotes = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_dislikes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private List<Article> downVotes = new ArrayList<>();

}