package com.example.paper.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Article{
    @Id
    @GeneratedValue
    private Long articleId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String account;      //作者的账号
    @Column(nullable = false)
    private String kind;
    @Column(nullable = false,columnDefinition = "text")
    private String content;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
}
