package com.example.paper.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Collect{
    @Id
    @GeneratedValue
    private  Long collectId;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private Long articleId;
    
}
