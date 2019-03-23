package com.example.paper.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ComposeForm {
    @NotEmpty(message = "标题不能为空")
    private String title;
    @NotEmpty(message = "种类不能为空")
    private String kind;
    @NotEmpty(message = "内容不能为空")
    private String  content;
}
