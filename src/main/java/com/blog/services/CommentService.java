package com.blog.services;

import com.blog.dto.CommentDto;
import com.blog.exception.ArticleException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface CommentService {
    CommentDto insert(CommentDto dto) throws JsonProcessingException, ArticleException;
    Map<String, Long> countCommentByListArticleId(List<String> ids);
    List<CommentDto> findByArticleId(String id);
    List<Object[]> countArticleByType();
}
