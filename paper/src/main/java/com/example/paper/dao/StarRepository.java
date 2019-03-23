package com.example.paper.dao;

import com.example.paper.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,String> {
    Star findByArticleIdAndAccount(String articleId,String Account);
    List<Star> findByArticleId(String articleId);
}
