package com.blog.util;

import com.blog.services.ResourceService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleDriver {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String APPLICATION_NAME = "EBook";
    private static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "sage-momentum-383822-62f0d56ce837.json";

    public File uploadFile(String folderId, String fileName, java.io.File fileToUpload) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        FileContent mediaContent = new FileContent("application/octet-stream", fileToUpload);

        File uploadedFile = driveService(resourceLoader).files().create(fileMetadata, mediaContent)
                .setFields("id, name, mimeType, webViewLink")
                .execute();

        return uploadedFile;
    }

    public File createFolder(String parentFolderId, String folderName) throws IOException {
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        folderMetadata.setParents(Collections.singletonList(parentFolderId));

        File createdFolder = driveService(resourceLoader).files().create(folderMetadata)
                .setFields("id, name, mimeType, webViewLink")
                .execute();

        return createdFolder;
    }

    private Drive driveService(ResourceLoader resourceLoader) throws IOException {
        // Load the service account key file as an input stream
        Resource resource = resourceLoader.getResource("classpath:" + SERVICE_ACCOUNT_KEY_FILE_PATH);

        // Create a GoogleCredential object from the input stream
        GoogleCredential credential = GoogleCredential.fromStream(resource.getInputStream())
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Build the Drive service object
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        Drive service = new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }

}
