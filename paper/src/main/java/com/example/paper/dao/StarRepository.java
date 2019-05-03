package com.example.paper.dao;

import com.example.paper.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarRepository extends JpaRepository<Star,Long> {
    Star findByArticleIdAndAccount(Long articleId,String Account);
    List<Star> findByArticleId(Long articleId);
}
