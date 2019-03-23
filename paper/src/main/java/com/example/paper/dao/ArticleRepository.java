package com.example.paper.dao;

import com.example.paper.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article,String> {
    Article findByArticleId(String  id);
    List<Article> deleteByArticleId(String id);
    List<Article> findByAccount(String id);
    Page<Article> findAllByKind(String  kind, Pageable pageable);
    Page<Article> findAll(Pageable pageable);
}
