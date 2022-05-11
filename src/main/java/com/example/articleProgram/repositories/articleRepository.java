package com.example.articleProgram.repositories;
import com.example.articleProgram.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface articleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a where a.author.id = ?1")
    public List<Article> getArticleBy (Long userId);

    @Query("SELECT a from Article a where a.title like %?1%")
    public List<Article> getArticleByTitle(String searchQuery);

    @Query(value = "select * from articles where user_id in (select following_id from user_following where user_id = ?1)", nativeQuery = true)
    public List<Article> getArticlesFromFollowings(Long id);

    @Query(value = "select count (article_id) from user_likes where article_id like ?1", nativeQuery = true)
    public int getNumberOfUpvotes (Long id);

    @Query(value = "select count (article_id) from user_dislikes where article_id like ?1", nativeQuery = true)
    public int getNumberOfDownVotes (Long id);
}
