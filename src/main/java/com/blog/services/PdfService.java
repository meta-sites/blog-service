package com.blog.services;

import com.blog.dto.PdfFileDto;
import com.blog.dto.PdfSearchDto;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PdfService {
    CompletableFuture<byte[]> downloadPdfByChunk(String fileName, Long chunkIndex, String fileId) throws BookException, JsonProcessingException;

    CompletableFuture<byte[]> downloadCVChunks(Long chunkIndex) throws BookException, JsonProcessingException;

    Boolean uploadFile(MultipartFile file, PdfFileDto dto) throws IOException;

    List<PdfFileDto> filter(PdfSearchDto dto);

    Boolean subscriber(String id) throws BookException, JsonProcessingException;

    List<String> findScribeByUserId(String id);

    byte[] accessResource(String coverFileName, Integer width) throws IOException, FileException;
}
