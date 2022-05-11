package com.example.articleProgram.DTO;

import com.example.articleProgram.model.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private Long id;
    private String username;
    private String email;
    private String jobDescription;
    private List<ListArticlesDTO> articles = new ArrayList<>();
}
