package com.blog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String articleId;
}