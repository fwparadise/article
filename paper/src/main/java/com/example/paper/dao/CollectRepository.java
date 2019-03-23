package com.example.paper.dao;

import com.example.paper.entity.Collect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectRepository extends JpaRepository<Collect,String> {
    Collect findByArticleIdAndAccount(String articleId,String account);
    List<Collect> findByAccount(String account);
    List<Collect> deleteByArticleIdAndAccount(String articleId,String account);
    List<Collect> deleteByArticleId(String id);
}
