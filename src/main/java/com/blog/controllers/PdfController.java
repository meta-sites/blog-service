package com.blog.controllers;

import com.blog.dto.PdfFileDto;
import com.blog.dto.PdfSearchDto;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.services.PdfService;
import com.blog.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping()
public class PdfController {

    private PdfService pdfService;

    @Autowired
    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping(value = "/private/api/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity< byte[] > downloadPdfChunks(@RequestParam("fileName") String fileName, @RequestParam("fileId") String fileId,
                                                      @RequestParam("chunk") Long chunkIndex) throws BookException, ExecutionException, InterruptedException, JsonProcessingException {
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(pdfService.downloadPdfByChunk(fileName, chunkIndex, fileId).get());
    }

    @PostMapping("/private/api/pdf")
    public ResponseEntity< Boolean > uploadFiles(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("metadata") String dto) throws IOException {
        PdfFileDto pdfFileDto = MapperUtil.mapStringToObject(dto, PdfFileDto.class);
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(pdfService.uploadFile(file, pdfFileDto));
    }

    @PostMapping("/public/api/pdf/filter")
    public ResponseEntity<List<PdfFileDto>> filter(@RequestBody PdfSearchDto dto) {
        return ResponseEntity
                .ok()
                .body(pdfService.filter(dto));
    }

    @PostMapping("/private/api/pdf/subscriber/{id}")
    public ResponseEntity<Boolean> subscriber(@PathVariable ("id") String id) throws BookException, JsonProcessingException {
        return ResponseEntity
                .ok()
                .body(pdfService.subscriber(id));
    }

    @GetMapping("/public/api/pdf/cover/{coverFileName}")
    public ResponseEntity< byte[] > getPdfResource(@PathVariable String coverFileName) throws IOException, BookException, FileException {
        byte[] resource = pdfService.accessResource(coverFileName);
        return ResponseEntity
                .ok()
                .contentLength(resource.length)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
}
