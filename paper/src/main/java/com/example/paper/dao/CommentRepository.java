package com.example.paper.dao;

import com.example.paper.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,String> {
    List<Comment> findByArticleIdOrderByTime(String id);
    List<Comment> deleteByArticleId(String id);
}