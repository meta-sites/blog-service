package com.blog.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class DriveConfiguration {

    // Define the path to the service account key file as a constant
    private static final String SERVICE_ACCOUNT_KEY_FILE_PATH = "sage-momentum-383822-62f0d56ce837.json";
    // Define the application name as a constant
    private static final String APPLICATION_NAME = "EBook";

//    @Bean
//    public Drive driveService(ResourceLoader resourceLoader) throws IOException {
//        // Load the service account key file as an input stream
//        Resource resource = resourceLoader.getResource("classpath:" + SERVICE_ACCOUNT_KEY_FILE_PATH);
//
//        // Create a GoogleCredential object from the input stream
//        GoogleCredential credential = GoogleCredential.fromStream(resource.getInputStream())
//                .createScoped(Collections.singleton(DriveScopes.DRIVE));
//
//        // Build the Drive service object
//        HttpTransport transport = new NetHttpTransport();
//        JsonFactory jsonFactory = new JacksonFactory();
//        Drive service = new Drive.Builder(transport, jsonFactory, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        return service;
//    }
}

