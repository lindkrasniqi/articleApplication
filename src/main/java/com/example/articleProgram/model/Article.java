package com.example.articleProgram.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_title")
    private String title;

    @Column(name = "article_date")
    private LocalDateTime dateTimePublished;

    @Column(name = "article_description")
    private String description;

    @OneToMany(mappedBy = "article")
    @JsonManagedReference
    private List<Comments> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User author;

    @ManyToMany(mappedBy = "favorites")
    private List<User> favorites = new ArrayList<>();

    @ManyToMany(mappedBy = "upVotes")
    private List<User> upVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "downVotes")
    private List<User> downVotes = new ArrayList<>();

    private int up_votes;
    private int down_votes;

}
