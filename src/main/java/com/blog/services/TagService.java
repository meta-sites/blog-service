package com.blog.services;

import com.blog.dto.TagsDto;

import java.util.List;

public interface TagService {
    void analyzeTag();

    List< TagsDto > findAll();
}
