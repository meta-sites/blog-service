package com.blog.services;

import com.blog.dto.LikeDto;
import com.blog.exception.ArticleException;
import com.blog.exception.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LikeService {
    LikeDto insert(LikeDto dto) throws JsonProcessingException, ArticleException, UserException;

    Boolean remove(LikeDto dto) throws JsonProcessingException;

    Boolean isLike(String articleId, String userId);
}
