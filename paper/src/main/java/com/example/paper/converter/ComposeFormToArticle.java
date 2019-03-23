package com.example.paper.converter;

import com.example.paper.entity.Article;
import com.example.paper.form.ComposeForm;
public class ComposeFormToArticle {
    public  static Article convert(ComposeForm composeForm){
        Article article=new Article();
        article.setContent(composeForm.getContent());
        article.setKind(composeForm.getKind());
        article.setTitle(composeForm.getTitle());
        return article;
    }
}
