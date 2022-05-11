package com.example.articleProgram.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListArticlesDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateTimePublished;
    private int up_votes;
    private int down_votes;
}