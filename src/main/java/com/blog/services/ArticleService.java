package com.blog.services;

import com.blog.dto.*;
import com.blog.exception.ArticleException;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.models.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    ArticleDto insert(ArticleDto dto) throws JsonProcessingException;

    ArticleDto update(ArticleDto dto) throws ArticleException, JsonProcessingException;

    List< ArticleShortDto > filter(ArticleSearchDto dto) throws ArticleException;

    Boolean share(String id);

    ArticleDto getOne(String urlFriendly) throws ArticleException, JsonProcessingException;

    SummaryArticleDto summary();

    PdfFileDto uploadArticleImage(MultipartFile file) throws IOException;

    byte[] getArticleImageCover(String id) throws IOException, FileException, BookException;
}
