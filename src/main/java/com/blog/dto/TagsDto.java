package com.blog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagsDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Long appearance;
}
