package com.example.paper.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Message{
    @Id
    @GeneratedValue
    private Long MessageId;
    @Column(nullable = false)
    private String receiver;
    @Column(nullable = false)
    private String content;
}
