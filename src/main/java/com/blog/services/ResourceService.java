package com.blog.services;

import com.blog.exception.FileException;

import java.io.IOException;

public interface ResourceService {
    byte[] accessResource(String path) throws IOException, FileException;

    byte[] accessImage(String path, Integer width) throws IOException, FileException;

    void removeResource(String path) throws IOException;

    String getResourcePath(String path) throws IOException;
}
