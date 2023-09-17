package com.blog.controllers;

import com.blog.exception.FileException;
import com.blog.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


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

    @GetMapping(value = {"/assets/images/**"})
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException, FileException {
        String requestPath = request.getRequestURI();
        String[] pathSegments = requestPath.split("/");
        String fileName = pathSegments[pathSegments.length - 1];
        String filePath = staticSource + "/assets/images/" + fileName;
        MediaType contentType;
        if (fileName.endsWith(".svg")) {
            contentType = MediaType.parseMediaType("image/svg+xml");
        } else if (fileName.endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF;
        } else if (fileName.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG;
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(resourceService.accessResource(filePath));
    }

    @GetMapping(value = {"/*.*.ttf", "*.ico"})
    public ResponseEntity<byte[]> getTTF(HttpServletRequest request) throws IOException, FileException {
        String requestPath = request.getRequestURI();
        String[] pathSegments = requestPath.split("/");
        String fileName = pathSegments[pathSegments.length - 1];
        String filePath = staticSource + fileName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(fileName).build());

        return new ResponseEntity<>(resourceService.accessResource(filePath), headers, HttpStatus.OK);
    }

}
