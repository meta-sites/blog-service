package com.blog.controllers;

import com.blog.dto.TagsDto;
import com.blog.exception.BookException;
import com.blog.services.TagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/public/api/tags")
public class TagsController {

    private TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity< List< TagsDto > > getPdfResource() {
        return ResponseEntity
                .ok()
                .body(tagService.findAll());
    }
}
