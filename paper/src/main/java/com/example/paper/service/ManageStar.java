package com.example.paper.service;

import com.example.paper.dao.StarRepository;
import com.example.paper.entity.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ManageStar {
    private final StarRepository starRepository;

    @Autowired
    public ManageStar(StarRepository starRepository) {
        this.starRepository = starRepository;
    }

    public boolean exist(String account,String  articleId){
        return starRepository.findByArticleIdAndAccount(articleId,account)!=null;
    }
    public float average(String  articleId){
        List<Star> list=starRepository.findByArticleId(articleId);
        int l=list.size();
        if (l==0){
            return 0;
        }
        else{
            float sum=0;
            for (Star star:list){
                sum+=star.getScore();
            }
            return sum/l;
        }
    }
    public boolean add(Star star){
        if (!exist(star.getAccount(),star.getArticleId())) {
            starRepository.save(star);
            return true;
        }
        else{
            return false;
        }
    }
}
