package com.blog.job;

import com.blog.repositories.ArticleRepository;
import com.blog.services.ResourceService;
import com.blog.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;


@Component
@Slf4j
public class BackupJob {

    private ArticleRepository articleRepository;
    private ResourceService resourceService;
    private GoogleDriver googleDriver;

    public BackupJob(ArticleRepository articleRepository, ResourceService resourceService, GoogleDriver googleDriver) {
        this.articleRepository = articleRepository;
        this.resourceService = resourceService;
        this.googleDriver = googleDriver;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void backup() throws IOException, InterruptedException {
        String backupFolder = resourceService.getResourcePath(ResourceConstants.BACK_UP);
        FileUtil.deleteAllFilesInFolder(backupFolder);

        backupDataForSpecificTables(backupFolder);
        backupImages(backupFolder);
        uploadSourceBackupToDriver(backupFolder);
    }

    private void backupImages(String backupFolderPath) throws IOException {
        String articleCoverDir = resourceService.getResourcePath(ResourceConstants.ARTICLE_DIR_COVER);
        String userLogoDir = resourceService.getResourcePath(ResourceConstants.USER_DIR_LOGO);
        String ebookDirCover = resourceService.getResourcePath(ResourceConstants.EBOOK_DIR_COVER);
        String ebookDirPdf = resourceService.getResourcePath(ResourceConstants.EBOOK_DIR_PDF);
        List<String> pathList = Arrays.asList(articleCoverDir, userLogoDir, ebookDirCover, ebookDirPdf);

        FileOutputStream fos = new FileOutputStream(new File(backupFolderPath, ResourceConstants.BACK_UP_ZIP_FILE_NAME));
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        TarArchiveOutputStream tos = new TarArchiveOutputStream(bos);

        String parentPath = articleCoverDir;
        for(String path: pathList) {
            parentPath = UtilFunction.findLongestCommonSubstring(parentPath, path);
        }

        for(String path: pathList) {
            FileUtil.addToTarGzFile(path, tos, path.replace(parentPath, ""));
        }
        tos.close();
        fos.close();
    }

    private void uploadSourceBackupToDriver(String backupFilePath) throws IOException {
        String subFolderBackUp = buildSubFolderName();
        com.google.api.services.drive.model.File folder = googleDriver.createFolder(resourceService.getResourcePath(ResourceConstants.BACKUP_FOLDER_ID), subFolderBackUp);
        String folderId = folder.getId();

        googleDriver.uploadFile(folderId, ResourceConstants.BACK_UP_SQL_FILE_NAME, new File(backupFilePath.concat(ResourceConstants.BACK_UP_SQL_FILE_NAME)));
        googleDriver.uploadFile(folderId, ResourceConstants.BACK_UP_ZIP_FILE_NAME, new File(backupFilePath.concat(ResourceConstants.BACK_UP_ZIP_FILE_NAME)));
    }

    private String buildSubFolderName() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        return "back-up-" + formattedDate;
    }

    private void backupDataForSpecificTables(String backupFolderPath) throws IOException, InterruptedException {
        String backupFileName = ResourceConstants.BACK_UP_SQL_FILE_NAME;
        String username = resourceService.getResourcePath(ResourceConstants.DB_USERNAME);
        String password = resourceService.getResourcePath(ResourceConstants.DB_PASSWORD);
        String databaseName = resourceService.getResourcePath(ResourceConstants.DB_NAME);
        String dbHost = resourceService.getResourcePath(ResourceConstants.DB_HOST);
        String dbPort = resourceService.getResourcePath(ResourceConstants.DB_PORT);

        String[] tablesToBackup = {"article", "book_subscriber", "comment", "like", "pdf_file", "rate_tags", "user_entity"};

        StringBuilder commandBuilder = new StringBuilder("mysqldump");
        commandBuilder.append(" --user=").append(username);
        commandBuilder.append(" --password=").append(password);
        commandBuilder.append(" --host=").append(dbHost);
        commandBuilder.append(" --port=").append(dbPort);
        commandBuilder.append(" --compact --no-create-info");
        commandBuilder.append(" ").append(databaseName);

        for (String table : tablesToBackup) {
            commandBuilder.append(" ").append(table);
        }

        commandBuilder.append(" -r ").append(backupFolderPath).append(backupFileName);

        String command = commandBuilder.toString();
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }
}
