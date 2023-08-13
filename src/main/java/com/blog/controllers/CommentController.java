package com.blog.controllers;

import com.blog.dto.CommentDto;
import com.blog.dto.UserDto;
import com.blog.exception.ArticleException;
import com.blog.services.CommentService;
import com.blog.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/api/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<CommentDto> insert(@RequestBody CommentDto dto) throws JsonProcessingException, ArticleException {
        return ResponseEntity
                .ok()
                .body(commentService.insert(dto));
    }
}
