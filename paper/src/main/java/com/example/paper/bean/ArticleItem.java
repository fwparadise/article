package com.example.paper.bean;

import lombok.Data;

import java.util.Date;
@Data
public class ArticleItem {
    private Long articleId;
    private String title;
    private String author;
    private String content;
    private Date createTime;
    private String kind;
}
