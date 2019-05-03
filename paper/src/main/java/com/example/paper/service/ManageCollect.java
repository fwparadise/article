package com.example.paper.service;

import com.example.paper.bean.ArticleItem;
import com.example.paper.dao.ArticleRepository;
import com.example.paper.dao.CollectRepository;
import com.example.paper.dao.UserRepository;
import com.example.paper.entity.Article;
import com.example.paper.entity.Collect;
import com.example.paper.entity.User;
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

    public boolean exist(String account,Long articleId) {
        return collectRepository.findByArticleIdAndAccount(articleId, account) != null;
    }

    public List<ArticleItem> getCollect(String account) {
        List<Collect> collectList = collectRepository.findByAccount(account);
        List<ArticleItem> entryArticleList = new ArrayList<>();
        for (Collect collect : collectList) {
            Article article = articleRepository.findByArticleId(collect.getArticleId());
            User user = userRepository.findByAccount(collect.getAccount());
            ArticleItem entryarticle = new ArticleItem();
            entryarticle.setAuthor(user.getUsername());
            entryarticle.setTitle(article.getTitle());
            entryarticle.setCreateTime(article.getCreateTime());
            entryarticle.setArticleId(article.getArticleId());
            entryArticleList.add(entryarticle);
        }
        return entryArticleList;
    }

    public boolean delete(String account, Long articleId) {
        return collectRepository.deleteByArticleIdAndAccount(articleId, account).size() > 0;
    }
    public void delete(Long id){
        collectRepository.deleteByArticleId(id);
    }

    public boolean add(String account, Long articleId) {
        Collect collect = new Collect();
        collect.setAccount(account);
        collect.setArticleId(articleId);
        collectRepository.save(collect);
        return true;
    }
}
