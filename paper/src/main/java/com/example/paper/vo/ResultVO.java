package com.example.paper.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class ResultVO implements Serializable {
    private Integer state;
    private String msg;
    private String token="";
    private Object data;
}
