package com.example.articleProgram.services;

import com.example.articleProgram.DTO.GetCommentsDTO;
import com.example.articleProgram.DTO.PostCommentDTO;
import com.example.articleProgram.model.Article;
import com.example.articleProgram.model.Comments;
import com.example.articleProgram.model.User;
import com.example.articleProgram.repositories.articleRepository;
import com.example.articleProgram.repositories.commentsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class commentsService {
    private final com.example.articleProgram.repositories.articleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final com.example.articleProgram.repositories.userRepository userRepository ;
    private final com.example.articleProgram.repositories.commentsRepository commentsRepository;

    public commentsService(com.example.articleProgram.repositories.articleRepository articleRepository, ModelMapper modelMapper, com.example.articleProgram.repositories.userRepository userRepository, com.example.articleProgram.repositories.commentsRepository commentsRepository) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
    }

    public GetCommentsDTO postNewComment (PostCommentDTO postCommentDTO, Article article, User user) {
        Comments comments = modelMapper.map(postCommentDTO, Comments.class);
        comments.setArticle(article);
        comments.setUser(user);
        comments.setDatePublished(LocalDateTime.now());
        comments.setUpVotes(0);
        comments.setDownVotes(0);
        commentsRepository.save(comments);
        article.getComments().add(comments);
        return modelMapper.map(comments, GetCommentsDTO.class);
    }

    public GetCommentsDTO updateComment (PostCommentDTO postCommentDTO, Long commentId, User user) {
        Optional<Comments> comments = commentsRepository.findById(commentId);
        System.out.println(comments.get().getUser().getId());
        System.out.println(user.getId());
        if (comments.get().getUser().getId().equals(user.getId())) {
            comments.get().setContent(postCommentDTO.getContent());
            commentsRepository.save(comments.get());
            return modelMapper.map(comments.get(), GetCommentsDTO.class);
        }
        else {
            return null;
        }
    }
}
