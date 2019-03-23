package com.example.paper.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultComment implements Serializable {
    private String username;
    private String content;
}
