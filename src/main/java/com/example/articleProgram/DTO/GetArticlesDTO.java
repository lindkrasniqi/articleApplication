package com.example.articleProgram.DTO;

import com.example.articleProgram.model.Comments;
import com.example.articleProgram.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetArticlesDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateTimePublished;
    private List<GetCommentsDTO> comments = new ArrayList<>();
    private UserListDTO author;
    private int up_votes;
    private int down_votes;
}
