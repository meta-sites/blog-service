package com.blog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SummaryArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long numberArticleJava;
    private Long numberViewJava;
    private Long numberCommentJava;

    private Long numberArticleOOP;
    private Long numberViewOOP;
    private Long numberCommentOOP;

    private Long numberArticleDevOP;
    private Long numberViewDevOP;
    private Long numberCommentDevOP;

    private Long numberArticleJs;
    private Long numberViewJs;
    private Long numberCommentJs;
}
