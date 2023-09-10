package com.blog.controllers;

import com.blog.exception.FileException;
import com.blog.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FrontendController {

    @Autowired
    private ResourceService resourceService;

    @Value("${blog.static.source}")
    private String staticSource;

    @GetMapping(value = {"/", "/blog/**", "/blogger/**", "/tai-lieu/**", "admin/**"})
    public String blog() throws IOException, FileException {
        String filePath = staticSource + "/index.html";
        return new String(resourceService.accessResource(filePath), StandardCharsets.UTF_8);
    }

    @GetMapping(value = {"/*.js"})
    public ResponseEntity<byte[]> getJS(HttpServletRequest request) throws IOException, FileException {
        String requestPath = request.getRequestURI();
        String[] pathSegments = requestPath.split("/");
        String fileName = pathSegments[pathSegments.length - 1];

        String filePath = staticSource + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/javascript"));
        return new ResponseEntity<>(resourceService.accessResource(filePath), headers, HttpStatus.OK);
    }

    @GetMapping(value = {"/*.css"})
    public ResponseEntity<byte[]> getCSS(HttpServletRequest request) throws IOException, FileException {
        String requestPath = request.getRequestURI();
        String[] pathSegments = requestPath.split("/");
        String fileName = pathSegments[pathSegments.length - 1];

        String filePath = staticSource + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/css"));
        return new ResponseEntity<>(resourceService.accessResource(filePath), headers, HttpStatus.OK);
    }


}
