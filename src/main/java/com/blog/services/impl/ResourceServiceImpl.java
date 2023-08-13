package com.blog.services.impl;

import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.services.ResourceService;
import com.blog.util.ExceptionConstants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ResourceServiceImpl implements ResourceService {
    private ResourceLoader resourceLoader;

    public ResourceServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public byte[] accessResource(String path) throws IOException, FileException {
        String resourcePath = new File(path).getAbsolutePath();
        Resource resource = resourceLoader.getResource("file:" + resourcePath);

        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            }
        } else {
            throw new FileException(ExceptionConstants.BOOK_IS_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }
}
