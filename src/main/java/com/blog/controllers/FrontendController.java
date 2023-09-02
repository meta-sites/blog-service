package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FrontendController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(value = {"/", "/blog/**", "/blogger/**", "/tai-lieu/**", "admin/**"})
    public String blog() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/index.html");
        Path filePath = resource.getFile().toPath();
        return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
    }


}
