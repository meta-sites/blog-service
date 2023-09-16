package com.blog.services.impl;

import com.blog.exception.BookException;
import com.blog.exception.FileException;
import com.blog.services.ResourceService;
import com.blog.util.ExceptionConstants;
import com.blog.util.FileUtil;
import com.blog.util.PathUtil;
import com.blog.util.ResourceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${backup.dir}")
    private String backupDir;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${db.name}")
    private String dbName;

    @Value("${ebook.dir}")
    private String ebookDir;

    @Value("${article.dir}")
    private String articleDir;

    @Value("${users.dir}")
    private String userDir;

    @Value("${ebook.pdf.dir}")
    private String ebookPdfDir;

    @Value("${ebook.cover.dir}")
    private String ebookCoverDir;

    @Value("${article.cover.dir}")
    private String articleCoverDir;

    @Value("${user.logo.dir}")
    private String userLogoDir;

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.port}")
    private String dbPort;

    @Value("${backup.folder.id}")
    private String backupFolderId;

    @Value("${server.domain.url}")
    private String serverDomainUrl;

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
            throw new FileException(ExceptionConstants.RESOURCE_IS_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void removeResource(String path) {

    };

    public String getResourcePath(String path) throws IOException {
        String source;
        switch (path) {
            case ResourceConstants.BACK_UP:
                source = backupDir;
                FileUtil.createDirectoryIfNoExist(source);
                break;

            case ResourceConstants.DB_PASSWORD:
                source = dbPassword;
                break;

            case ResourceConstants.DB_USERNAME:
                source = dbUsername;
                break;

            case ResourceConstants.DB_NAME:
                source = dbName;
                break;

            case ResourceConstants.EBOOK_DIR:
                source = ebookDir;
                break;

            case ResourceConstants.ARTICLE_DIR:
                source = articleDir;
                break;

            case ResourceConstants.USER_DIR:
                source = userDir;
                break;

            case ResourceConstants.USER_DIR_LOGO:
                source = userLogoDir;
                break;

            case ResourceConstants.EBOOK_DIR_PDF:
                source = ebookPdfDir;
                break;

            case ResourceConstants.EBOOK_DIR_COVER:
                source = ebookCoverDir;
                break;

            case ResourceConstants.ARTICLE_DIR_COVER:
                source = articleCoverDir;
                break;

            case ResourceConstants.DB_HOST:
                source = dbHost;
                break;

            case ResourceConstants.DB_PORT:
                source = dbPort;
                break;

            case ResourceConstants.BACKUP_FOLDER_ID:
                source = backupFolderId;
                break;

            case ResourceConstants.SERVER_DOMAIN_URL:
                source = serverDomainUrl;
                break;

            default: source = "";
        }

        return source;
    };
}
