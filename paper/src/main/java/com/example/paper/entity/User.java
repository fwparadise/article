package com.example.paper.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class User implements Serializable {
    @Id
    private String  account;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;
    @Column
    private int sex;   //1为男
    @Column
    private String description;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition="mediumblob")
    private byte[] profile;
}
