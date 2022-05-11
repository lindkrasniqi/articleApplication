package com.example.articleProgram.DTO;

import com.example.articleProgram.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostArticleDTO {
    private String title;
    private String description;
}
