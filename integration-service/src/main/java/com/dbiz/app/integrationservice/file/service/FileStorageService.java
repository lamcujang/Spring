package com.dbiz.app.integrationservice.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    Path storeFile(MultipartFile file, String fileName);

    String deleteFile(String fileName);

}
