package com.example.articleProgram.services;

import com.example.articleProgram.DTO.*;
import com.example.articleProgram.model.Article;
import com.example.articleProgram.model.Comments;
import com.example.articleProgram.model.User;
import com.example.articleProgram.repositories.articleRepository;
import com.example.articleProgram.repositories.commentsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class articleService {
    private final articleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final com.example.articleProgram.repositories.userRepository userRepository ;
    private final commentsRepository commentsRepository;
    public articleService(com.example.articleProgram.repositories.articleRepository articleRepository, ModelMapper modelMapper, com.example.articleProgram.repositories.userRepository userRepository, com.example.articleProgram.repositories.commentsRepository commentsRepository) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.commentsRepository = commentsRepository;
    }

    public List<GetArticlesDTO> getArticles () {
        return articleRepository.findAll()
                .stream().map(article -> modelMapper.map(article, GetArticlesDTO.class)).collect(Collectors.toList());
    }

    public GetArticlesDTO updateArticle (String id, PostArticleDTO postArticleDTO, User user) {
        Long articleId = Long.parseLong(id);
        Optional <Article> x = articleRepository.findById(articleId);
        if (x.isPresent() && user.getId().equals(x.get().getAuthor().getId())) {
            x.get().setTitle(postArticleDTO.getTitle());
            x.get().setDescription(postArticleDTO.getDescription());
            articleRepository.save(x.get());
            user.getArticles().add(x.get());
            return modelMapper.map(x.get(), GetArticlesDTO.class);

        }else {
            return null;
        }
    }
    public Article deleteArticle (String id, User user) {
        Long articleId = Long.parseLong(id);
        Optional <Article> x = articleRepository.findById(articleId);
        if (x.isPresent() && user.getId().equals(x.get().getAuthor().getId())) {
            user.getArticles().remove(x.get());
            articleRepository.delete(x.get());
            return x.get();
        }else {
            return null;
        }
    }
    public List<GetArticlesDTO> getArticleByTitle (String searchQuery) {
        return articleRepository.getArticleByTitle(searchQuery)
                .stream().map(article -> modelMapper.map(article, GetArticlesDTO.class)).collect(Collectors.toList());
    }

    public List<GetArticlesDTO> getFeedArticles (Long id) {
        return articleRepository.getArticlesFromFollowings(id).stream()
                .map(article -> modelMapper.map(article, GetArticlesDTO.class)).collect(Collectors.toList());
    }

    public ListArticlesDTO addArticleToFavorites (User u, Long articleId) {
        u.getFavorites().add(articleRepository.findById(articleId).get());
        //articleRepository.findById(articleId).get().getFavorites().add(u);
        userRepository.save(u);
        //articleRepository.save(articleRepository.findById(articleId).get());
        return modelMapper.map(articleRepository.findById(articleId).get(), ListArticlesDTO.class);
    }
//
    public List<ListArticlesDTO> getFavoriteArticles (User u) {
        return u.getFavorites().stream().map(article -> modelMapper.map(article, ListArticlesDTO.class)).collect(Collectors.toList());
    }

    public ListArticlesDTO upVoteArticle (User user, Long articleId) {
        Optional<Article> a = articleRepository.findById(articleId);
            if (!hasAlreadyUpVoted(user, a.get())) {
                return addUpVote(user, a.get());
            }
            if (hasAlreadyDownVoted(user, a.get())) {
                return removeDownVote(user, a.get());
            }
            else {
                return null;
            }
    }

    public ListArticlesDTO downVoteArticle (User user, Long articleId) {
        Optional<Article> a = articleRepository.findById(articleId);
        if (hasAlreadyUpVoted(user, a.get())) {
            return removeUpVote(user, a.get());
        }
        if (!hasAlreadyDownVoted(user, a.get())) {
            return addDownVote(user, a.get());
        }
        else {
            return null;
        }
    }

    private ListArticlesDTO addUpVote (User user, Article article) {
        user.getUpVotes().add(article);
        article.getUpVotes().add(user);
        userRepository.save(user);
        article.setUp_votes(articleRepository.getNumberOfUpvotes(article.getId()));
        articleRepository.save(article);
        return modelMapper.map(article, ListArticlesDTO.class);
    }

    private ListArticlesDTO removeUpVote (User user, Article article) {
        user.getUpVotes().remove(article);
        article.getUpVotes().remove(user);
        user.getDownVotes().add(article);
        article.getUpVotes().add(user);
        userRepository.save(user);
        article.setUp_votes(articleRepository.getNumberOfUpvotes(article.getId()));
        articleRepository.save(article);
        return modelMapper.map(article, ListArticlesDTO.class);
    }

    private ListArticlesDTO addDownVote (User user, Article article) {
        user.getDownVotes().add(article);
        article.getDownVotes().add(user);
        userRepository.save(user);
        article.setDown_votes(articleRepository.getNumberOfDownVotes(article.getId()));
        articleRepository.save(article);
        return modelMapper.map(article, ListArticlesDTO.class);
    }
    private ListArticlesDTO removeDownVote (User user, Article article) {
        user.getDownVotes().remove(article);
        article.getDownVotes().remove(user);
        user.getUpVotes().add(article);
        article.getUpVotes().add(user);
        userRepository.save(user);
        article.setUp_votes(articleRepository.getNumberOfUpvotes(article.getId()));
        articleRepository.save(article);
        return modelMapper.map(article, ListArticlesDTO.class);
    }

    private boolean hasAlreadyUpVoted (User user, Article a) {
        for (User u : a.getUpVotes()) {
            if (u.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
    private boolean hasAlreadyDownVoted (User user, Article a) {
        for (User u : a.getDownVotes()) {
            if (u.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }


}
