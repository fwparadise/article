package com.example.paper.entity;

import lombok.Data;

@Data
public class ArticleModified {
    private String articleId;
    private String title;
    private String kind;
    private String content;
}
