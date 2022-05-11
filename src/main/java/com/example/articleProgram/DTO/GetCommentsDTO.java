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
public class GetCommentsDTO {

    private Long id;
    private String content;
    private LocalDateTime datePublished;
    private int upVotes;
    private int downVotes;
    private UserListDTO user;

}
