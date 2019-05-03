
package com.example.paper.service;
import com.example.paper.dao.CommentRepository;
import com.example.paper.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ManageComment {
    private final CommentRepository commentRepository;

    @Autowired
    public ManageComment(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getList(Long  id){
        return commentRepository.findByArticleIdOrderByTime(id);
    }
    public void addComment(Comment comment){
        commentRepository.save(comment);
    }
    public void deleteComment(Long id){
        commentRepository.deleteByArticleId(id);
    }
}
