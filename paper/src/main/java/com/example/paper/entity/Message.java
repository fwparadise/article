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
@GenericGenerator(name = "java-uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class Message implements Serializable {
    @Id
    @GeneratedValue(generator = "java-uuid")
    private String id;
    @Column(nullable = false)
    private String receiver;
    @Column(nullable = false)
    private String content;
}
