package com.blog.services.impl;

import com.blog.aop.anotation.IncreaseSubscribeBook;
import com.blog.dto.PdfFileDto;
import com.blog.dto.PdfSearchDto;
import com.blog.enums.PdfTypeEnum;
import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.models.PdfFile;
import com.blog.models.User;
import com.blog.repositories.PdfFileRepository;
import com.blog.repositories.UserRepository;
import com.blog.services.AuthenticationService;
import com.blog.services.CacheService;
import com.blog.services.PdfService;
import com.blog.services.ResourceService;
import com.blog.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@Slf4j
public class PdfServiceImpl implements PdfService {

    @Value("${ebook.pdf.dir}")
    private String eBookDir;

    @Value("${ebook.cover.dir}")
    private String coverDir;

    private PdfFileRepository pdfFileRepository;
    private UserRepository userRepository;
    private AuthenticationService authenticationService;
    private CacheService cacheService;
    private ResourceService resourceService;
    private static final int CHUNK_SIZE = 3 * 1024 * 1024;
    private WeakHashMap<String, RandomAccessFile> fileCache = new WeakHashMap<>();

    public PdfServiceImpl(PdfFileRepository pdfFileRepository, AuthenticationService authenticationService,
                          UserRepository userRepository, CacheService cacheService, ResourceService resourceService) {
        this.pdfFileRepository = pdfFileRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.resourceService = resourceService;
    }

    private RandomAccessFile getOrCreateRandomAccessFile(String fileName) {
        return fileCache.computeIfAbsent(fileName, key -> {
            try {
                return new RandomAccessFile(eBookDir + key, "r");
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<byte[]> downloadFileByChunk(String fileName, Long chunkIndex, String fileId) throws BookException, JsonProcessingException {
        if (!isSubscribe(fileId)) throw new BookException(ExceptionConstants.NOT_SUB_BOOK, HttpStatus.UNAUTHORIZED);

        return CompletableFuture.supplyAsync(() -> {
            try {
                RandomAccessFile file = getOrCreateRandomAccessFile(fileName);
                byte[] buffer = new byte[CHUNK_SIZE];
                long offset = chunkIndex * CHUNK_SIZE;
                file.seek(offset);
                int bytesRead = file.read(buffer);
                if (bytesRead == -1) {
                    throw new IllegalArgumentException("Invalid chunk index");
                }
                return Arrays.copyOf(buffer, bytesRead);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<byte[]> downloadPdfByChunk(String fileName, Long chunkIndex, String fileId) throws BookException, JsonProcessingException {
        return downloadFileByChunk(fileName, chunkIndex, fileId);
    }

    @Override
    public CompletableFuture<byte[]> downloadCVChunks(Long chunkIndex) throws JsonProcessingException, BookException {
        return downloadFileByChunk("CV.pdf", chunkIndex, null);
    }

    @Override
    public Boolean uploadFile(MultipartFile file, PdfFileDto dto) throws IOException {
        dto.setLogoPath(savePdfCover(file));
        savePdfBook(file);
        PdfFile pdfFile = createPdfEntity(dto, file);
        pdfFileRepository.save(pdfFile);

        return Boolean.TRUE;
    }

    private void savePdfBook(MultipartFile file) throws IOException {
        log.info("Tạo folder ở " + eBookDir);
        FileUtil.createDirectoryIfNoExist(eBookDir);
        String fileName = file.getOriginalFilename();
        String filePath = eBookDir + fileName;
        file.transferTo(Paths.get(filePath));
    }

    private String savePdfCover(MultipartFile file) throws IOException {
        return FileUtil.exportFirstPdfPage(file.getInputStream(), coverDir, 1);
    }

    private PdfFile createPdfEntity(PdfFileDto dto, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        PdfFile pdfFile = MapperUtil.map(dto, PdfFile.class);
        pdfFile.setFileName(fileName);
        pdfFile.setFileSize(Long.valueOf(file.getSize()));
        pdfFile.setNumSub(0L);
        pdfFile.setIsPublic(false);
        pdfFile.setFileType(PdfTypeEnum.BOOK);

        return pdfFile;
    }

    @Override
    public List<PdfFileDto> filter(PdfSearchDto dto) {
        boolean isFilterByType = isFilterByType(dto);
        boolean isFilterByFileName = isFilterByFileName(dto);
        boolean isFilterBySubscribe = isFilterBySubscribe(dto);
        boolean isFilterByTxtSearch = isFilterByTxtSearch(dto);

        if (isFilterByType) return filterByType(dto);
        if (isFilterByFileName) return filterByFileName(dto);
        if (isFilterBySubscribe) return filterBySubscribe(dto);
        if (isFilterByTxtSearch) return filterByTxtSearch(dto);

        return new ArrayList<>();
    };

    @Transactional
    @Override
    @IncreaseSubscribeBook
    public Boolean subscriber(String id) throws BookException, JsonProcessingException {
        Optional<PdfFile> pdfFile = pdfFileRepository.findById(id);
        if (!pdfFile.isPresent())
            throw new BookException(ExceptionConstants.BOOK_IS_NOT_EXIST, HttpStatus.NOT_FOUND);
        PdfFile entity = pdfFile.get();
        User user = authenticationService.getCurrentUserForSave();
        pdfFileRepository.subscribedPdfFile(user.getId(), entity.getId());

        List<String> bookSubscript = getSubscriptInfo(user.getId());
        if (!bookSubscript.contains(entity.getId()))
            bookSubscript.add(entity.getId());
        cacheService.setCache(new Pair<>(buildKeyCacheSubscribe(user.getId()), StringUtils.join(bookSubscript, Constants.COMMA_CHARACTER)));
        return true;
    };

    @Override
    public List<String> findScribeByUserId(String id) {
        return getSubscriptInfo(id);
    };

    @Override
    public byte[] accessResource(String coverFileName) throws IOException, FileException {
        return resourceService.accessResource(coverDir + coverFileName);
    }

    private boolean isFilterByType(PdfSearchDto dto) {
        return Objects.nonNull(dto.getType()) && isLimitNumberPdfFile(dto);
    }

    private boolean isFilterByFileName(PdfSearchDto dto) {
        return Objects.nonNull(dto.getName()) && isLimitNumberPdfFile(dto);
    }

    private boolean isFilterBySubscribe(PdfSearchDto dto) {
        return dto.isFilterBySubscribe() && isLimitNumberPdfFile(dto);
    }

    private boolean isFilterByTxtSearch(PdfSearchDto dto) {
        return StringUtils.isNotBlank(dto.getTextSearch()) && isLimitNumberPdfFile(dto);
    }

    private boolean isLimitNumberPdfFile(PdfSearchDto dto) {
        return Objects.nonNull(dto.getNumsPerPage()) && Objects.nonNull(dto.getPageNumber());
    }

    private List<PdfFileDto> filterByType(PdfSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return MapperUtil.mapAll(pdfFileRepository.findByFileType(dto.getType(), pageable), PdfFileDto.class);
    }

    private List<PdfFileDto> filterByFileName(PdfSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return MapperUtil.mapAll(pdfFileRepository.findByName(dto.getName(), pageable), PdfFileDto.class);
    };

    private List<PdfFileDto> filterBySubscribe(PdfSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return MapperUtil.mapAll(pdfFileRepository.filterBySubscribe(pageable), PdfFileDto.class);
    };

    private List<PdfFileDto> filterByTxtSearch(PdfSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getNumsPerPage());
        return MapperUtil.mapAll(pdfFileRepository.filterByTxtSearch(dto.getTextSearch(), pageable), PdfFileDto.class);
    }

    private String buildKeyCacheSubscribe(String userId) {
        return CacheConstants.SUBSCRIBE_CACHE + userId;
    }

    private Boolean isSubscribe(String fileId) throws JsonProcessingException {
        User user = authenticationService.getCurrentUser();
        return getSubscriptInfo(user.getId()).contains(fileId);
    }

    private List<String> getSubscriptInfo(String userId) {
        String bookSubscribe;
        List<String> subscribeList;
        bookSubscribe = cacheService.getCache(buildKeyCacheSubscribe(userId));

        if (Objects.nonNull(bookSubscribe)) {
             return new ArrayList<>(Arrays.asList(bookSubscribe.split(Constants.COMMA_CHARACTER)));
        } else {
            subscribeList = pdfFileRepository.findScribeByUserId(userId);
            cacheService.setCache(new Pair<>(buildKeyCacheSubscribe(userId), StringUtils.join(subscribeList, Constants.COMMA_CHARACTER)));
            return subscribeList;
        }
    }
}
