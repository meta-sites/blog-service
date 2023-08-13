package com.blog.controllers;

import com.blog.dto.CommentDto;
import com.blog.dto.LikeDto;
import com.blog.exception.ArticleException;
import com.blog.exception.UserException;
import com.blog.services.LikeService;
import com.blog.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/api/like")
public class LikeController {

    private LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping()
    public ResponseEntity< LikeDto > insert(@RequestBody LikeDto dto) throws JsonProcessingException, ArticleException, UserException {
        return ResponseEntity
                .ok()
                .body(likeService.insert(dto));
    }

    @PostMapping("/remove")
    public ResponseEntity< Boolean > remove(@RequestBody LikeDto dto) throws JsonProcessingException, ArticleException {
        return ResponseEntity
                .ok()
                .body(likeService.remove(dto));
    }
}
