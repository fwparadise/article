package com.example.paper.bean;

import com.example.paper.entity.Article;
import lombok.Data;

@Data
public class ArticleDetail {
    private Article article;
    private String author;
}
