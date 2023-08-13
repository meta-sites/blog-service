package com.blog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String articleId;
    private String content;
}
