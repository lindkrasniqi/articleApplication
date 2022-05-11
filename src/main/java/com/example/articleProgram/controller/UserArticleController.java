package com.example.articleProgram.controller;
import com.example.articleProgram.DTO.*;
import com.example.articleProgram.model.Article;
import com.example.articleProgram.model.User;
import com.example.articleProgram.repositories.articleRepository;
import com.example.articleProgram.services.commentsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/articles")
public class UserArticleController {
    private final com.example.articleProgram.services.userService userService;
    private final com.example.articleProgram.services.articleService articleService;
    private final com.example.articleProgram.repositories.userRepository userRepository;
    private final articleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final com.example.articleProgram.services.commentsService commentsService;

    @PostMapping
    public ResponseEntity<ListArticlesDTO> publishArticle (@RequestBody PostArticleDTO postArticleDTO) {

        return ResponseEntity.ok(userService.publishArticle(postArticleDTO, getLoggedInUser()));
    }

    @GetMapping
    public ResponseEntity<List<GetArticlesDTO>> getArticles () {
        return ResponseEntity.ok().body(articleService.getArticles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetArticlesDTO> updateArticle (@PathVariable("id") String id, @RequestBody PostArticleDTO postArticleDTO) {
        if (articleService.updateArticle(id, postArticleDTO, getLoggedInUser()) != null) {
            return ResponseEntity.ok().body(articleService.updateArticle(id, postArticleDTO, getLoggedInUser()));
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle (@PathVariable String id) {
        if (articleService.deleteArticle(id, getLoggedInUser()) != null) {
            return ResponseEntity.ok().body(articleService.deleteArticle(id, getLoggedInUser()));
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/{id}")
    public ResponseEntity postComment (@RequestBody @Validated PostCommentDTO postCommentDTO,
                                                 @PathVariable(name = "id", required = true) String article_id) {

        Optional<Article> article = articleRepository.findById(Long.parseLong(article_id));
        if (article.isPresent()) {
            return ResponseEntity.ok(commentsService.postNewComment(postCommentDTO, article.get(), getLoggedInUser()));
        }
        else {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("Error Message", "Article not found with that id");
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<GetArticlesDTO>> getArticlesByTitle (@PathVariable(name = "name") String searchQuery) {
        return ResponseEntity.ok().body(articleService.getArticleByTitle(searchQuery));
    }

    @PutMapping("/{articleId}/{commentId}")
    public ResponseEntity updateComment (@PathVariable(name = "articleId") String articleId,
                                                         @PathVariable(name = "commentId") String commentId, @RequestBody PostCommentDTO postCommentDTO
                                                         ) {
        if (commentsService.updateComment(postCommentDTO, Long.parseLong(commentId), getLoggedInUser()) == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error_message", "You are unauthorized");
                return ResponseEntity.status(FORBIDDEN).body(error);
        }
        else {
            return ResponseEntity.ok(commentsService.updateComment(postCommentDTO, Long.parseLong(commentId), getLoggedInUser()));
        }
    }
    @PostMapping("/{id}/favorites")
    public ResponseEntity<ListArticlesDTO> makeAarticleFavorite (@PathVariable (name = "id") String articleId) {
        return ResponseEntity.ok().body(articleService.addArticleToFavorites(getLoggedInUser(), Long.parseLong(articleId)));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<ListArticlesDTO>> getFavArticles () {
        return ResponseEntity.ok().body(articleService.getFavoriteArticles(getLoggedInUser()));
    }

    @PutMapping("/{id}/upVote")
    public ResponseEntity upVoteArticle (@PathVariable(name = "id") String articleId) {
        ListArticlesDTO ld = articleService.upVoteArticle(getLoggedInUser(), Long.parseLong(articleId));
        if (ld == null) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "You already upvoted this article");
            return ResponseEntity.badRequest().body(error);
        }else {
            return ResponseEntity.ok().body(ld);
        }
    }
    @PutMapping("/{id}/downVote")
    public ResponseEntity downVoteArticle (@PathVariable(name = "id") String articleId) {
        ListArticlesDTO ld = articleService.downVoteArticle(getLoggedInUser(), Long.parseLong(articleId));
        if (ld == null) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "You already downvoted this article");
            return ResponseEntity.badRequest().body(error);
        }else {
            return ResponseEntity.ok().body(ld);
        }
    }
    private User getLoggedInUser () {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user= null;
        for (User u : userRepository.findAll()) {
            if (u.getUsername().equals(username)) {
                user = u;
            }
        }
        return user;
    }
}
