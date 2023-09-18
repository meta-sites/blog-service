package com.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String articleId;
    private String content;
    private String authorName;
    private String imgUrl;
    private Date createAt;

    public CommentDto() {
    }

    public CommentDto(String articleId, String content, String authorName, String imgUrl, Date createAt) {
        this.articleId = articleId;
        this.content = content;
        this.authorName = authorName;
        this.imgUrl = imgUrl;
        this.createAt = createAt;
    }
}
