package com.example.paper.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@GenericGenerator(name = "java-uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(generator = "java-uuid")
    private String id;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String articleId;
    @Column(nullable = false)
    private String content;
    @CreatedDate
    private Date time;
}
