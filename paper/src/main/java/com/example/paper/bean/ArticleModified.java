package com.example.paper.bean;

import lombok.Data;

@Data
public class ArticleModified {
    private Long articleId;
    private String title;
    private String kind;
    private String content;
}
