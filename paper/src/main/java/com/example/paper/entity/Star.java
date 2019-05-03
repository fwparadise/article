package com.example.paper.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
//@GenericGenerator(name = "java-uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class Star {
    @Id
    @GeneratedValue
    private Long starId;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private Long articleId;
    @Column(nullable = false)
    private int score;
}
