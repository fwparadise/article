package com.example.paper.service;

import com.example.paper.dao.ArticleRepository;
import com.example.paper.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ManageArticle {
    private final ArticleRepository articleRepository;

    @Autowired
    public ManageArticle(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article addArticle(Article article){
        return articleRepository.save(article);
    }
    public void updateArticle(Article article){
        articleRepository.save(article);
    }
    public Article findOne(Long id){
        return articleRepository.findByArticleId(id);
    }
    public void deleteOne(Long id){
        articleRepository.deleteByArticleId(id).size();
    }
    public Page<Article> getList(String kind, int page, int size){
        Pageable pageable=PageRequest.of(page-1,size,Sort.Direction.DESC,"updateTime");
        if (kind.equals("全部")){
            return articleRepository.findAll(pageable);
        }
        else {
            return articleRepository.findAllByKind(kind,pageable);
        }
    }
    public List<Article> getCreated(String account){
        return articleRepository.findByAccount(account);
    }
}
