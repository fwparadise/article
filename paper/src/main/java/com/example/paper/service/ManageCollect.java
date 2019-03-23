package com.example.paper.service;

import com.example.paper.dao.ArticleRepository;
import com.example.paper.dao.CollectRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.entity.Article;
import com.example.paper.entity.Collect;
import com.example.paper.entity.User;
import com.example.paper.entity.entryArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ManageCollect {
    private final CollectRepository collectRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ManageCollect(CollectRepository collectRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.collectRepository = collectRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public boolean exist(String account, String articleId) {
        if (collectRepository.findByArticleIdAndAccount(articleId, account) == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<entryArticle> getCollect(String account) {
        List<Collect> collectList = collectRepository.findByAccount(account);
        List<entryArticle> entryArticleList = new ArrayList<>();
        for (Collect collect : collectList) {
            Article article = articleRepository.findByArticleId(collect.getArticleId());
            User user = userRepository.findByAccount(collect.getAccount());
            entryArticle entryarticle = new entryArticle();
            entryarticle.setAuthor(user.getUsername());
            entryarticle.setTitle(article.getTitle());
            entryarticle.setCreateTime(article.getCreateTime());
            entryarticle.setArticleId(article.getArticleId());
            entryArticleList.add(entryarticle);
        }
        return entryArticleList;
    }

    public boolean delete(String account, String articleId) {
        return collectRepository.deleteByArticleIdAndAccount(articleId, account).size() > 0;
    }
    public boolean delete(String id){
        collectRepository.deleteByArticleId(id);
        return true;
    }

    public boolean add(String account, String articleId) {
        Collect collect = new Collect();
        collect.setAccount(account);
        collect.setArticleId(articleId);
        collectRepository.save(collect);
        return true;
    }
}
