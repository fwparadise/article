package com.example.paper.dao;

import com.example.paper.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticleIdOrderByTime(Long id);
    List<Comment> deleteByArticleId(Long id);
}