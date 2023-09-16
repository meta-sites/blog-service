package com.blog.controllers;

import com.blog.dto.*;
import com.blog.exception.ArticleException;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.services.ArticleService;
import com.blog.services.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class ArticleController {

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/private/api/article")
    public ResponseEntity< ArticleDto > insert(@RequestBody ArticleDto articleDto) throws IOException {
        return ResponseEntity
                .ok()
                .body(articleService.insert(articleDto));
    }

    @PutMapping("/private/api/article")
    public ResponseEntity< ArticleDto > update(@RequestBody ArticleDto articleDto) throws ArticleException, JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(articleService.update(articleDto));
    }

    @PutMapping("/private/api/article/{id}/share")
    public ResponseEntity< Boolean > share(@PathVariable String id) {
        return ResponseEntity
                .ok()
                .body(articleService.share(id));
    }

    @PostMapping("/public/api/article")
    public ResponseEntity< List< ArticleShortDto > > filter(@RequestBody ArticleSearchDto articleSearchDto) throws ArticleException {
        return ResponseEntity
                .ok()
                .body(articleService.filter(articleSearchDto));
    }

    @GetMapping("/public/api/article/{urlFriendly}")
    public ResponseEntity< ArticleDto > getOne(@PathVariable String urlFriendly) throws ArticleException, JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(articleService.getOne(urlFriendly));
    }

    @GetMapping("/public/api/article/summary")
    public ResponseEntity< SummaryArticleDto > summary() throws ArticleException, JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(articleService.summary());
    }

    @PostMapping("/private/api/article/upload-article-image")
    public ResponseEntity< PdfFileDto > uploadArticleImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity
                .ok()
                .body(articleService.uploadArticleImage(file));
    }

    @GetMapping("/public/api/article/article-image-cover/{id}")
    public ResponseEntity< byte[] > getArticleImageCover(@PathVariable String id) throws IOException, FileException, BookException {
        return ResponseEntity
                .ok()
                .body(articleService.getArticleImageCover(id));
    }
}
