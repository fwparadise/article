package com.example.paper.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class entryArticle implements Serializable {
    private String title;
    private String author;
    private Date createTime;
    private String articleId;
}
